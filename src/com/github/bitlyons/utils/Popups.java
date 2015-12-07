package com.github.bitlyons.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
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
        label.setTextAlignment(TextAlignment.CENTER);
        Button yesButton = new Button("Yes");
        yesButton.setMinWidth(90);// i find 90 to be a nice size
        Button noButton = new Button("No");
        noButton.setMinWidth(90);

        yesButton.setOnAction(e -> {
            yesNo = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            yesNo =false;
            window.close();
        });

        //Layout
        VBox layout = new VBox(15); //using a VBox to set the message on top of the buttons
        HBox layout2 = new HBox(10); //using a HBox to set the buttons side by side
        layout2.getChildren().addAll(yesButton, noButton);
        layout2.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label,layout2); //adds the message and the previous layout that holds our buttons
        layout.setPadding(new Insets(20, 10, 10, 20));
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
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("OK!");
        okButton.setMinWidth(90);
        okButton.setOnAction(e -> window.close());

        //Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(label,okButton);
        layout.setAlignment(Pos.CENTER);

        //Set the scene.
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
