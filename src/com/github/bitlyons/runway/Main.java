package com.github.bitlyons.runway;

import com.github.bitlyons.runway.ui.WindowObjects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Project Runway 2 : Airline Management Software!");
        HBox hbox = new HBox(5);
        BorderPane layout = new BorderPane();
        layout.setBottom(hbox);

        MenuBar topMenu = WindowObjects.topMenu();
        layout.setTop(topMenu);
        /** Show the main window */
        Scene scene = new Scene(layout, 800, 600);
        //TODO create new stylesheet to theme the program
        //scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        //Stop java from closing the window, and then proceed to use our own method.
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            exit(primaryStage);
        });
    }

    //this function will be called when the user try's to exit the program.
    //passing it a stage to avoid a global stage like in version 1.0
    public void exit(Stage window){
        window.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
