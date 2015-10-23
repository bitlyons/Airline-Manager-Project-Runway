package com.github.bitlyons.utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by : Brendan Lyons (20066143)
 * About: contains our popup windows, Alerts and Confirm boxes.
 */
public class Popups {
    static boolean yesNo;

    /** usage: boolean return = Confirm("Title", "The message to display"); */
    public static boolean confirm(String windowTitle, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle(windowTitle);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            yesNo = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            yesNo =false;
            window.close();
        });

        //Layout
        VBox layout = new VBox(15); //using a Vbox to set the message on top of the buttons
        HBox layout2 = new HBox(10); //using a HBox to set the buttons side by side
        layout2.getChildren().addAll(yesButton, noButton);
        layout2.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label,layout2); //adds the message and the previous layout that holds our buttons
        layout.setAlignment(Pos.CENTER);

        //Set the scene.
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();// window needs to be closed
        return yesNo;
    }

    /** usage: Alert("Title", "The message to display"); */
    public static void alert(String windowTitle, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle(windowTitle);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button okButton = new Button("OK!");
        okButton.setOnAction(e -> window.close());

        //Layout
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label,okButton);
        layout.setAlignment(Pos.CENTER);

        //Set the scene.
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
