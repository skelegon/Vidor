package org.steve.sulev;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/***
 * Importance of this class: Used to modify application settings parameters
 */
public class SettingsWindow extends Stage {

    private Settings settings;
    private final ObservableList<String> listItems = FXCollections.observableArrayList();

    @FXML
    private Label thumbnailcountlabel;

    @FXML
    private Slider thumbnailwidthslider;

    @FXML
    private Label thumbnailwidthlabel;

    @FXML
    private TextField vlclocationtextfield;

    @FXML
    private ColorPicker thumbnailhighlightcolorpicker;

    @FXML
    private ListView dirlist;

    @FXML
    private Slider thumbnailcountslider;

    // Closes the "Settings configruation" window when the "Close" button is pressed.
    public void onSettingsCloseAction(ActionEvent actionEvent) {
        close();
    }

    //Saves info from UI back to settings class, closes the stage / returns to caller.
    public void onSettingsSaveAction(ActionEvent actionEvent) {
        settings.setVlcLocation(vlclocationtextfield.getText());
        settings.setThumbnailCount((int)thumbnailcountslider.getValue());
        settings.setThumbnailWidth((int)thumbnailwidthslider.getValue());
        settings.setThumbnailHighlightColor(thumbnailhighlightcolorpicker.getValue().toString());
        settings.setFolders(Arrays.asList(listItems.toArray(new String[listItems.size()])));
        close();
    }

    // Allows browsing for the VLC media player file location and displays the chosen location in the referred text field.
    public void onVlcBrowseAction(ActionEvent actionEvent) {
        File file = new FileChooser().showOpenDialog(this);
        if(file != null){
            vlclocationtextfield.setText(file.getAbsolutePath());
        }
    }

    // Allows browsing for directories of video files and creates a list of the chosen directories.
    public void onLocationAddAction(ActionEvent actionEvent) {
        File dir = new DirectoryChooser().showDialog(this);

        if(dir != null){
            listItems.add(dir.getAbsolutePath());
        }
    }

    // Allows removing directories from the list and opens a warning window when the button is pressed but an item is not chosen.
    public void onLocationRemoveAction(ActionEvent actionEvent) {
        int item = dirlist.getSelectionModel().getSelectedIndex();
        if(item != -1){
            listItems.remove(item);
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(null);
            a.setHeaderText(null);
            a.setContentText("You need to select an item in order to remove it.");
            a.showAndWait();
        }
    }

    public Settings getSettings() {return settings;}

    //Class constructor, settings class is passed from caller also parent window to block caller when this stage is open.
    public SettingsWindow(Window owner, Settings s){
        setTitle("Settings configuration");
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        setWidth(600);
        setHeight(400);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        loader.setController(this);

        try {
            setScene(new Scene(loader.load()));
        } catch (IOException ex){
            ex.printStackTrace();
        }

        settings = s;
        init();
    }

    //General setup, adds passed in settings info to UI, maps value listeners for slider UI elements.
    private void init() {
        //VLC Location
        if(!settings.getVlcLocation().trim().isEmpty()){
            vlclocationtextfield.setText(settings.getVlcLocation());
        }

        //Thumbnail Count
        thumbnailcountslider.valueProperty().addListener((value, oldValue, newValue) -> {
            thumbnailcountlabel.setText("(" + newValue.intValue() + ")");
        });

        thumbnailcountslider.setValue(settings.getThumbnailCount());

        //Thumbnail Width
        thumbnailwidthslider.valueProperty().addListener((value, oldValue, newValue) -> {
            thumbnailwidthlabel.setText("(" + newValue.intValue() + ")");
        });

        thumbnailwidthslider.setValue(settings.getThumbnailWidth());

        //Thumbnail Color Picker
        thumbnailhighlightcolorpicker.setValue(Color.web(settings.getThumbnailHighlightColor()));

        //Video Locations
        if(settings.getFolders() != null) {
            listItems.addAll(settings.getFolders());
        }
        dirlist.setItems(listItems);
    }
}
