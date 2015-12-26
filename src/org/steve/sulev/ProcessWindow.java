package org.steve.sulev;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
public class ProcessWindow extends Stage{
    private int width;
    private int count;

    private Map<String, List<File>> dbFiles = new HashMap<>();

    @FXML
    private ProgressBar progress;

    @FXML
    private Label processmessage;

    public ProcessWindow(Window owner, Map<String, List<File>> files, int width, int count){
        setTitle("Checking database integrity.");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        initStyle(StageStyle.UNDECORATED);
        setWidth(600);
        setHeight(90);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("process.fxml"));
        loader.setController(this);

        try
        {
            setScene(new Scene(loader.load()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.dbFiles = files;
        this.width = width;
        this.count = count;
        init();
    }

    private Task task = new Task<Void>() {
        @Override public Void call() {
            JdbcConnectionSource cs = null;
            try {
                cs = new JdbcConnectionSource("jdbc:sqlite:vidor.db");
                Dao<File, Integer> fileDao = DaoManager.createDao(cs, File.class);
                Dao<Folder, Integer> folderDao = DaoManager.createDao(cs, Folder.class);
                Dao<Frame, Integer> frameDao = DaoManager.createDao(cs, Frame.class);

                for (Map.Entry<String, List<File>> entries : dbFiles.entrySet()) {
                    int fileCount = 0;
                    int max = entries.getValue().size();
                    Folder folder = null;
                    try {
                        folder = folderDao.queryBuilder().where().eq(Folder.FOLDER_LOCATION, entries.getKey()).queryForFirst();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(folder == null){
                        folder = new Folder(entries.getKey());
                    }
                    try {
                        folderDao.createIfNotExists(folder);
                        for (File file : entries.getValue()) {
                            if(file.getMissing()){
                                fileDao.delete(file);
                            } else {
                                file.setFolder(folder);
                                fileDao.createOrUpdate(file);

                                List<Frame> frames = genThumbs(folder.getLocation() + "/" + file.getName(), count, width, file.getId());

                                if(frames.size() > 0) {
                                    int frameCount = 0;
                                    for (Frame frame : frames) {
                                        frameDao.create(frame);
                                        updateMessage(String.format("Processing folder: %s%nFile: %s", entries.getKey(), file.getName() + " (" + ++frameCount + " / "+ count +")"));
                                    }
                                } else {
                                    System.out.println(file.getName() + ": 0 frames, skipping");
                                    fileDao.delete(file);
                                }
                            }
                            updateProgress(++fileCount, max);
                        }
                    } catch (SQLException | InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException ex){
                ex.printStackTrace();
            } finally {
                try {
                    if(cs != null) cs.close();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
            return null;
        }
    };

    private List<Frame> genThumbs(String file, int count, int width, int fileid) throws IOException, InterruptedException {
        List<Frame> frames = new ArrayList<>();

        ProcessBuilder p = new ProcessBuilder("ffmpeg.exe", "-i", file);
        Process process = p.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        int duration = 0;

        while ((line = br.readLine()) != null) {
            if(line.contains("decoding") && line.contains("failed")) break;

            if(line.contains("Duration")) {
                String[] d = line.trim().split("[ .]")[1].split(":");
                duration = 3600 * Integer.parseInt(d[0]) + 60 * Integer.parseInt(d[1]) + Integer.parseInt(d[2]);
            }
        }

        process.waitFor();

        if(duration > 0) {
            int limit = duration / count;
            for(int i = 1; i <= count; i++){
                int seconds = limit * i;

                //System.out.println(String.format("PIC: ffmpeg.exe -ss %s -i \"%s\" -vf scale=%s:-1 -vframes 1 -y \"thumbnails/%s.jpg\"", Utilities.formatSeconds(seconds), file, width, i));

                p = new ProcessBuilder("ffmpeg.exe",
                        "-ss",
                        Utilities.formatSeconds(seconds),
                        "-i",
                        file,
                        "-vf",
                        "scale=" + width + ":-1",
                        "-vframes",
                        "1",
                        "-y",
                        "thumbnails/" + i + ".jpg"
                );

                process = p.start();
                br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                //Dirty hack
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                }

                process.waitFor();

                BufferedImage img = ImageIO.read(new java.io.File(String.format("thumbnails/%s.jpg", i)));
                Graphics g = img.getGraphics();

                Font font = new Font("Arial", Font.PLAIN, 20);

                g.setColor(new Color(255,255,255,80));
                g.fillRect(0, 10, img.getWidth(), 26);

                g.setColor(Color.BLACK);
                g.setFont(font);
                g.drawString(Utilities.formatSeconds(seconds), 10, 30);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);

                frames.add(new Frame(fileid, seconds, baos.toByteArray()));
                g.dispose();
            }
        }
        return frames;
    }

    private void init() {
        progress.progressProperty().bind(task.progressProperty());
        processmessage.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> this.close());

        Thread t = new Thread(task);
        t.start();
    }

}
