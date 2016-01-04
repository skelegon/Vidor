package org.steve.sulev;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Window;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/***
 * MainController class sets actions for main (main.fxml) UI elements.
 * Entry point to all other functions.
 */

public class MainController {
    private Settings settings;
    private ObservableList<String> tagList = FXCollections.observableArrayList();

    //Structures to hold file and folder information
    private Map<String, List<File>> diskFiles = new HashMap<>();
    private Map<String, List<File>> dbFiles = new HashMap<>();

    //DB Data access objects
    private Dao<File, Integer> fileDao;
    private Dao<Folder, Integer> folderDao;
    private Dao<Frame, Integer> frameDao;
    private Dao<Tag, Integer> tagDao;
    private Dao<VideoTags, Integer> videoTagsDao;

    @FXML
    private AnchorPane stage;

    @FXML
    private TreeTableView<File> filelist;

    @FXML
    private ScrollPane scroll;

    @FXML
    private FlowPane flow;

    @FXML
    private ComboBox tagcombo;

    @FXML
    private FlowPane tagbox;

    @FXML
    private TextField searchbox;

    /***
     * Opens settings window and passes settings parameters.
     * Waits until window is closed.
     * Reads values from SettingsWindow class back to settings variable.
     * Saves settings to external file.
     * Initializes intcheck.
     */
    public void onSettingsAction(ActionEvent actionEvent) {
        Window owner = stage.getScene().getWindow();
        SettingsWindow s = new SettingsWindow(owner, settings);
        s.showAndWait();
        settings = s.getSettings();
        Utilities.saveSettings(settings);
        intcheck();
    }

    // Controls for the "Add" button. Adds the selected tag to the selected video.
    public void onTagAddAction(ActionEvent actionEvent) {
        File f = filelist.getSelectionModel().selectedItemProperty().getValue().getValue();
        try {
            Tag t = tagDao.queryBuilder().where().eq(Tag.TAG_NAME, tagcombo.getSelectionModel().selectedItemProperty().getValue()).queryForFirst();
            videoTagsDao.create(new VideoTags(f,t));
            createTagButton(f, t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Controls for the "Manage" button. Opens the tags window.
    public void onTagsManageAction(ActionEvent actionEvent) {
        Window owner = stage.getScene().getWindow();
        TagsWindow s = new TagsWindow(owner);
        s.showAndWait(); //Blocks the caller until the modal stage is closed
        tagList.clear();
        tagList.addAll(s.getTags());
    }

    // Pressing the "Search" button runs the "updateList" method. Only videos with tags assigned in the searchbox are displayed. If the searchbox is empty, all videos are shown.
    public void onSearchAction(ActionEvent actionEvent) {
        if(searchbox.getText().isEmpty()){
            updateList(dbFiles);
        } else {
            updateList(searchFiles(searchbox.getText()));
        }
    }

    //Opens my github page
    public void onAboutAction(ActionEvent actionEvent) {
        Utilities.openGit();
    }

    //Closes the application
    public void onCloseAction(ActionEvent actionEvent) { System.exit(0); }

    /***
     * This is activated by FXMLLoader when fxml file is loaded.
     * Loads settings from external file (vidor.config) via Utilities class.
     * Binds scroll element size with flow element size.
     * Calls run() method at some unspecified time in the future.
     * (Basically when entire fxml is loaded to prevent nullpointer exceptions)
     */
    public void initialize() {
        settings = Utilities.loadSettings();
        scroll.viewportBoundsProperty().addListener((bounds, oldBounds, newBounds) -> {
            flow.setPrefWidth(newBounds.getWidth());
            flow.setPrefHeight(newBounds.getHeight());
        });
        Platform.runLater(this::run);
    }

    /***
     * Initializes database
     * populates tagList (Observablelist)
     * performs initial integrity check
     */
    private void run() {
        setupDatabase();
        try {
            tagList.addAll(tagDao.queryForAll().stream().map(Tag::getName).collect(Collectors.toList()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tagcombo.setItems(new SortedList<String>(tagList, Collator.getInstance()));
        intcheck();
    }

    //Checks the integrity of the database and updates file list on UI
    private void intcheck() {
        diskFiles.clear(); //Questionable? Proper way to clean maps needs research.

        for(Iterator<String> iter = settings.getFolders().iterator(); iter.hasNext(); ){
            String f = iter.next();
            Path p = Paths.get(f);
            if(Files.exists(p)){
                diskFiles.put(f, Utilities.getFilesFromDisk(p));
            } else {
                iter.remove();
                Utilities.saveSettings(settings);
            }
        }

        populateDbFiles();

        Map<String, List<File>> diff = compareLists(diskFiles, dbFiles);

        if(diff.size() != 0){
            Window owner = stage.getScene().getWindow();
            ProcessWindow p = new ProcessWindow(owner, diff, settings.getThumbnailWidth(), settings.getThumbnailCount());
            p.showAndWait();

            populateDbFiles();
            updateList(dbFiles);
        }
    }

    //Returns TreeItem, sets parent and adds icon
    private TreeItem<File> makeBranch(File file, TreeItem<File> parent, ImageView icon){
        TreeItem<File> item = new TreeItem<>(file, icon);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    /***
     * Updates values in UI TreeTableView element
     * Sets row selection event listener (Triggers if row is changed)
     * When event listener triggers then corresponding video frames (thumbnails) are retrieved from database,
     * made into ImageView elements and added to UI FlowPane element.
     * Each ImageView element has 3 eventlisteners:
     *      1. setOnMouseClicked (When image is clicked, opens VLC media player with corresponding video at certain time)
     *      2. setOnMouseEntered (When mouse moves on the picture, highlight effect is shown)
     *      3.setOnMouseExited (When mouse leaves the picture, highlight effect is removed)
     * Retrieves corresponding video tags and adds to tagbox FlowPane UI element.
     */
    public void updateList(Map<String, List<File>> files){
        TreeItem<File> root = new TreeItem<>(new File("Files", 0L, null));
        root.setExpanded(true);

        files.entrySet().forEach(f -> {
            TreeItem<File> folder = makeBranch(new File(f.getKey() + " (" + f.getValue().size() + ")", 0L, null), root, new ImageView(new Image(getClass().getResourceAsStream("/folder.png"))));

            for (File file : f.getValue()) {
                makeBranch(file, folder, new ImageView (new Image(getClass().getResourceAsStream("/files.png"))));
            }
        });

        TreeTableColumn<File, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setPrefWidth(400);
        nameColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<File, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().getName())
        );

        TreeTableColumn<File, String> sizeColumn = new TreeTableColumn<>("Filesize");
        sizeColumn.setPrefWidth(80);
        sizeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<File, String> param) -> new ReadOnlyStringWrapper(Utilities.bytesToHuman(param.getValue().getValue().getFileSize()))
        );

        filelist.setShowRoot(false);
        filelist.setRoot(root);
        filelist.getColumns().setAll(nameColumn, sizeColumn);

        filelist.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    File f = newValue.getValue();
                    if(f.getId() != 0){
                        flow.getChildren().clear();
                        List<Frame> frames = new ArrayList<>();
                        try {
                            frames = frameDao.queryBuilder().where().eq(Frame.FILE_ID, f.getId()).query();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        for(Frame frame : frames) {
                            try {
                                ImageView iv = new ImageView();
                                iv.setImage(Utilities.bytesToImage(frame.getThumbnail()));

                                iv.setId(String.format("--start-time,%s,\"%s\"", frame.getTime(), f.getFolder().getLocation() + "\\" + f.getName()));
                                iv.setOnMouseClicked(event -> Utilities.startVid(iv.getId(), settings.getVlcLocation()));

                                InnerShadow innerShadow = new InnerShadow();
                                innerShadow.setRadius(15.0);
                                innerShadow.setChoke(0.3);
                                innerShadow.setColor(Color.web(settings.getThumbnailHighlightColor()));

                                iv.setOnMouseEntered(event -> ((ImageView)event.getSource()).setEffect(innerShadow));
                                iv.setOnMouseExited(event -> ((ImageView)event.getSource()).setEffect(null));

                                flow.getChildren().add(iv);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        tagbox.getChildren().clear();
                        for(VideoTags t : f.getTags()){
                            createTagButton(f, t.getTag());
                        }
                    }
                });
    }

    /***
     * Creates video tag button
     * It's label element with button element inside it.
     * Inline CSS is added to set background color, add rounded corners and remove focus color.
     * Event listener with tag delete function is assigned to button element. (Delete also removes the label itself.)
     */
    public void createTagButton(File file, Tag tag){
        Button closeButton = new Button();
        closeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/close.png"))));
        closeButton.setStyle("-fx-padding: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: transparent");
        closeButton.setCursor(Cursor.HAND);

        Label label = new Label(tag.getName());
        label.setStyle("-fx-padding: 2px 5px 2px 10px; -fx-background-color: #fff; -fx-background-radius: 10;  -fx-border-radius: 10; -fx-border-color: #ccc");
        label.setGraphic(closeButton);
        label.setContentDisplay(ContentDisplay.RIGHT);

        closeButton.setOnAction(event -> {
            try {
                DeleteBuilder<VideoTags, Integer> deleteBuilder = videoTagsDao.deleteBuilder();
                deleteBuilder.where().eq(VideoTags.TAG_ID, tag.getId()).and().eq(VideoTags.FILE_ID, file.getId());
                deleteBuilder.delete();
                tagbox.getChildren().remove(label);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        tagbox.getChildren().add(label);
    }

    /***
     * Compares diskFiles and dbFiles HashMaps
     * Returns difference between given lists
     * Used in integrity check to keep database info same as on disk.
     */
    public Map<String, List<File>> compareLists(Map<String, List<File>> irl, Map<String, List<File>> db) {
        if (irl.size() == 0 && db.size() == 0) {
            return null;
        } else {
            if (irl.size() == 0) {
                db.entrySet().forEach(f -> f.getValue().forEach(p -> p.setMissing(true)));
                return db;
            } else if (db.size() == 0) {
                return irl;
            } else {
                db.entrySet().stream().filter(entry -> irl.keySet().contains(entry.getKey())).forEach(entry -> {
                    Iterator<File> iter = entry.getValue().iterator();
                    while (iter.hasNext()) {
                        File file = iter.next();
                        if (irl.get(entry.getKey()).contains(file)) {
                            iter.remove();
                            irl.get(entry.getKey()).remove(file);
                        } else {
                            file.setMissing(true);
                        }
                    }
                });

                for (Map.Entry<String, List<File>> entry : irl.entrySet()) {
                    if (db.keySet().contains(entry.getKey())) {
                        entry.getValue().stream().filter(file -> !db.get(entry.getKey()).contains(file)).forEach(file -> db.get(entry.getKey()).add(file));
                    } else {
                        db.put(entry.getKey(), entry.getValue());
                    }
                }
                return db;
            }
        }
    }

    //Populates dbFiles HashMap
    private void populateDbFiles() {
        dbFiles.clear(); //Perhaps not the best way to do it, needs more research
        try {
            List<Folder> folders = folderDao.queryForAll();
            for(Folder folder : folders){
                List<File> files = fileDao.queryBuilder().where().eq(File.FOLDER_ID, folder).query();
                dbFiles.put(folder.getLocation(), files);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    //Sets up database access objects and created data tables in Sqlite DB if missing.
    private void setupDatabase() {
        JdbcConnectionSource cs = null;
        try {
            cs = new JdbcConnectionSource("jdbc:sqlite:vidor.db");
            fileDao = DaoManager.createDao(cs, File.class);
            folderDao = DaoManager.createDao(cs, Folder.class);
            frameDao = DaoManager.createDao(cs, Frame.class);
            tagDao = DaoManager.createDao(cs, Tag.class);
            videoTagsDao = DaoManager.createDao(cs, VideoTags.class);

            TableUtils.createTableIfNotExists(cs, File.class);
            TableUtils.createTableIfNotExists(cs, Folder.class);
            TableUtils.createTableIfNotExists(cs, Frame.class);
            TableUtils.createTableIfNotExists(cs, Tag.class);
            TableUtils.createTableIfNotExists(cs, VideoTags.class);
        } catch (SQLException ex){
            ex.printStackTrace();
        } finally {
            try {
                if(cs != null) cs.close();
            } catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    /***
     * Search files from database that correspond with searchbox UI element value.
     * Splits searchbox string by comma and queries DB for files where videotag is equal to all parameters split that way.
     * returns HashMap
     */
    private Map<String, List<File>> searchFiles(String find){
        Map<String, List<File>> res = new HashMap<>();
        String[] parts = find.split(",");
        try {
            List<Tag> t = tagDao.queryBuilder().where().in(Tag.TAG_NAME, parts).query();
            List<VideoTags> vtl = videoTagsDao.queryBuilder()
                    .groupBy(VideoTags.FILE_ID).having(String.format("COUNT(%s) = %s", VideoTags.TAG_ID, t.size())).where()
                    .in(VideoTags.TAG_ID, t).query();
            for (VideoTags vt : vtl) {
                String folder = vt.getFile().getFolder().getLocation();
                if(!res.containsKey(folder)){
                    List<File> list = new ArrayList<>();
                    list.add(vt.getFile());
                    res.put(folder, list);
                } else {
                    res.get(folder).add(vt.getFile());
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return res;
    }
}
