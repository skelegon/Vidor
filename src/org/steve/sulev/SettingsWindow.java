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

/**
 * Created by Steve Sulev on 26.12.2015.
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

    public void onSettingsCloseAction(ActionEvent actionEvent) {
        close();
    }

    public void onSettingsSaveAction(ActionEvent actionEvent) {
        settings.setVlcLocation(vlclocationtextfield.getText());
        settings.setThumbnailCount((int)thumbnailcountslider.getValue());
        settings.setThumbnailWidth((int)thumbnailwidthslider.getValue());
        settings.setThumbnailHighlightColor(thumbnailhighlightcolorpicker.getValue().toString());
        settings.setFolders(Arrays.asList(listItems.toArray(new String[listItems.size()])));
        close();
    }

    public void onVlcBrowseAction(ActionEvent actionEvent) {
        File file = new FileChooser().showOpenDialog(this);
        if(file != null){
            vlclocationtextfield.setText(file.getAbsolutePath());
        }
    }

    public void onLocationAddAction(ActionEvent actionEvent) {
        File dir = new DirectoryChooser().showDialog(this);

        if(dir != null){
            listItems.add(dir.getAbsolutePath());
        }
    }

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
