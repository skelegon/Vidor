package org.steve.sulev;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Utilities {
    private static PropertiesConfiguration config = new PropertiesConfiguration();
    private Utilities(){}

    public static Settings loadSettings(){
        config.setListDelimiter('|');
        Settings s = new Settings();
        try {
            config.load("vidor.config");
        } catch (ConfigurationException ex){
            ex.printStackTrace();
        }

        s.setVlcLocation(config.getString("vlclocation", ""));
        s.setThumbnailCount(config.getInt("thumbnailcount", 10));
        s.setThumbnailWidth(config.getInt("thumbnailwidth", 200));
        s.setThumbnailHighlightColor(config.getString("thumbnailhighlightcolor", ""));
        s.setFolders(new ArrayList(Arrays.asList(config.getStringArray("folders"))));

        return s;
    }

    public static void saveSettings(Settings settings) {
        config.setProperty("vlclocation", settings.getVlcLocation());
        config.setProperty("thumbnailcount", settings.getThumbnailCount());
        config.setProperty("thumbnailwidth", settings.getThumbnailWidth());
        config.setProperty("thumbnailhighlightcolor", settings.getThumbnailHighlightColor());
        config.setProperty("folders", String.join("|", settings.getFolders()));

        try {
            config.save("vidor.config");
        } catch (ConfigurationException ex){
            ex.printStackTrace();
        }
    }

    public static List<File> getFilesFromDisk(Path p) {
        List<File> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p, "*.{wmv,mp4,flv,mov,avi,mkv}")) {
            for (Path entry : stream){
                result.add(new File(entry.getFileName().toString(), Files.size(entry), null));
            }
        } catch (IOException | DirectoryIteratorException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String formatSeconds(int s){
        return String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

    public static Image bytesToImage(byte[] bytes) throws IOException{
        return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytes)), null);
    }

    public static void startVid(String parameters) {
        String[] args = parameters.split(",");
        try {
            new ProcessBuilder("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe",
                    args[0],
                    args[1],
                    args[2]
            ).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman (long size)
    {
        if(size == 0) return "";

        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(size) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

    public static boolean containsCaseInsensitive(List<String> list, String strToCompare)
    {
        for(String str:list)
        {
            if(str.equalsIgnoreCase(strToCompare))
            {
                return true;
            }
        }
        return false;
    }
}
