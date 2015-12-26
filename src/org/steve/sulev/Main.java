package org.steve.sulev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // Starts the program and configures the main window title and size. Loads the layout file.
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Vidor 0.5");
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(1400);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
