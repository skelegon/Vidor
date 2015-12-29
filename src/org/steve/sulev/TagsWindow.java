package org.steve.sulev;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

/***
 * Importance of this class: Provides UI to Add and Remove tags from database.
 */

public class TagsWindow extends Stage {
    private final ObservableList<String> data = FXCollections.observableArrayList();
    private Dao<Tag, Integer> tagDao;

    @FXML
    private ListView tagslist;

    @FXML
    private TextField tagstextbox;

    public TagsWindow(Window owner){
        setResizable(false);
        setTitle("Tag manager");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("tags.fxml"));
        loader.setController(this);

        try
        {
            setScene(new Scene(loader.load()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        setupDatabase();
    }

    public ObservableList<String> getTags(){
        return data;
    }

    private void setupDatabase(){
        JdbcConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:vidor.db");
            tagDao = DaoManager.createDao(connectionSource, Tag.class);
            init();
        } catch (Exception ex){
            System.err.println(ex.getMessage());
        } finally {
            try{
                if (connectionSource != null) connectionSource.close();
            } catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }
    }

    private void init() {
        try {
            data.addAll(tagDao.queryForAll().stream().map(Tag::getName).collect(Collectors.toList()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tagslist.setItems(data);
    }

    public void onTagAddAction(ActionEvent actionEvent) {
        String val = tagstextbox.getText();
        if(!Utilities.containsCaseInsensitive(data, val)){
            try {
                tagDao.create(new Tag(val));
                data.add(val);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Already in: " + val);
        }
    }

    public void onTagRemoveAction(ActionEvent actionEvent) {
        int item = tagslist.getSelectionModel().getSelectedIndex();
        if(item != -1) {
            try {
                DeleteBuilder<Tag, Integer> deleteBuilder = tagDao.deleteBuilder();
                deleteBuilder.where().eq(Tag.TAG_NAME, data.get(item));
                deleteBuilder.delete();

                data.remove(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("You need to select an item in order to remove it.");
            alert.showAndWait();
        }
    }
}
