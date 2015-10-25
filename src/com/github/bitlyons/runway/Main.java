package com.github.bitlyons.runway;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Project Runway 2 : Airline Management Software!");
        HBox hbox = new HBox(5);
        BorderPane layout = new BorderPane();
        layout.setBottom(hbox);

        /** Show the main window */
        Scene scene = new Scene(layout, 800, 600);
        //TODO create new stylesheet to theme the program
        //scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
