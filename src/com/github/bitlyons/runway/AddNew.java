package com.github.bitlyons.runway;

import com.github.bitlyons.utils.Popups;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by : Brendan Lyons (20066143)
 */

public class AddNew {
    static String name, manufacturerName, airportAout, airportBout;
    static int age, passengerNo;
    static double salary, flightHour, pathTimeout;
    static boolean chiefStat, purserStat;

    public static Employee staff() {
        Employee newEmployee;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle("Add New Staff Member");

        //Layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(30, 30, 30, 30)); //30px padding around window
        layout.setVgap(10); //spacing between cells vertical
        layout.setHgap(10); //spacing between cells horizontal

        //name
        Label employeeNameLabel = new Label("Name : ");
        GridPane.setConstraints(employeeNameLabel, 0, 0);
        employeeNameLabel.setPrefWidth(100);
        TextField employeeName = new TextField();
        employeeName.setPrefWidth(300);  //should force the rest of the cells in 1,X to 300.
        employeeName.setPromptText("Enter Employee's Name!");
        GridPane.setConstraints(employeeName, 1, 0);

        //age
        Label employeeAgeLabel = new Label("Age  : ");
        GridPane.setConstraints(employeeAgeLabel, 0, 1);
        TextField employeeAge = new TextField();
        employeeAge.setPromptText("Enter Employee's Age");
        GridPane.setConstraints(employeeAge, 1, 1);

        employeeAge.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if (NewValue.equals("")) ; // workaround for empty text, still pops up if more text entered though
                else Integer.parseInt(NewValue);
            } catch (Exception e) {
                Popups.alert("Warning", "Value entered in Age is not a Number!");
            }
        });

        //salary
        Label employeeSalaryLabel = new Label("Salary  : ");
        GridPane.setConstraints(employeeSalaryLabel, 0, 2);
        TextField employeeSalary = new TextField();
        employeeSalary.setPromptText("Enter Employee's Salary");
        GridPane.setConstraints(employeeSalary, 1, 2);

        employeeSalary.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if (!NewValue.equals(""))Double.parseDouble(NewValue);
            } catch (Exception e) {
                Popups.alert("Warning", "Value entered in Salary is not a Number!");
            }
        });

        //role
        ComboBox<String> role = new ComboBox<>();
        Label roleLabel = new Label("Role  : ");
        GridPane.setConstraints(roleLabel, 0, 3);
        role.getItems().addAll("Pilot", "Attendant");
        role.setPromptText("Please Select the Role Of The Employee");
        GridPane.setConstraints(role, 1, 3);

        Button submit = new Button("Submit");
        GridPane.setColumnSpan(submit, 2);
        GridPane.setConstraints(submit, 0, 7);
        GridPane.setHalignment(submit, HPos.CENTER);


        //pilot flightHours
        Label flightHoursLabel = new Label("Flight Hours : ");
        GridPane.setConstraints(flightHoursLabel, 0, 4);
        TextField flightHours = new TextField();
        flightHours.setPromptText("Enter Employees total Flight Hours");
        GridPane.setConstraints(flightHours, 1, 4);


        flightHours.textProperty().addListener((v, oldValue, Newvalue) -> {
            try {
                if (Newvalue.equalsIgnoreCase("")) ;
                else Double.parseDouble(Newvalue);
            } catch (Exception e) {
                Popups.alert("Warning", "Value entered in Flight Hours is not a Number!");
            }
        });

        //Attendant chiefStatus, purserStatus
        CheckBox purserStatus = new CheckBox("Is A Purser");
        GridPane.setConstraints(purserStatus, 1, 4);
        CheckBox chiefStatus = new CheckBox("Is Chief Purser");
        GridPane.setConstraints(chiefStatus, 1, 5);
        chiefStatus.setDisable(true);

        purserStatus.selectedProperty().addListener((v, oldValue, NewValue) -> {
            if (NewValue) chiefStatus.setDisable(false);
            else chiefStatus.setDisable(true);
        });


        //add all non role specific to the layout
        layout.getChildren().addAll(employeeNameLabel, employeeName, employeeAgeLabel, employeeAge,
                employeeSalaryLabel, employeeSalary, roleLabel, role, submit);


        //listen for change in role
        role.getSelectionModel().selectedItemProperty().addListener((v, oldValue, NewValue) -> {
            if (role.getValue().equals("Attendant")) {
                layout.getChildren().removeAll(flightHoursLabel, flightHours);
                layout.getChildren().addAll(chiefStatus, purserStatus);
            }
            if (role.getValue().equals("Pilot")) {
                layout.getChildren().removeAll(chiefStatus, purserStatus);
                layout.getChildren().addAll(flightHoursLabel, flightHours);
            }
        });

        //submit button
        submit.setOnAction(e -> {
            try{
                name = employeeName.getText();
                age = Integer.parseInt(employeeAge.getText());
                salary = Double.parseDouble(employeeSalary.getText());
                //can leave these two here as the checkbox will be false
                chiefStat = chiefStatus.isSelected();
                purserStat = purserStatus.isSelected();
                window.close();
            }
            catch(Exception err){
                Popups.alert("Error", "Please fill out the Fields");
            }

        });

        //Set the scene.
        Scene scene = new Scene(layout, 500, 350);
        scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        try {
            if (role.getValue().equals("Pilot")) {
                //have to put flightHour here to avoid empty value.
                flightHour = Double.parseDouble(flightHours.getText());
                newEmployee = new Pilot(name, age, salary, flightHour);
                return newEmployee;
            } else {
                if (!purserStat) chiefStat = false; //since we disabled it, workaround to set it to false
                newEmployee = new Attendant(name, age, salary, chiefStat, purserStat);
                return newEmployee;
            }
        }
        catch(Exception e){
         //added for the case where the user just closes the window
        }

        //somehow no role was selected, return a dummy Employee.
        newEmployee = new Attendant("exitError88123", 0, 0, false, false);
        return newEmployee;
    }

    //Add new Plane

    public static Plane plane() {
        Plane newPlane;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle("Add New Plane");

        //Layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(30, 30, 30, 30)); //30px padding around window
        layout.setVgap(10); //spacing between cells vertical
        layout.setHgap(10); //spacing between cells horizontal

        //name
        Label planeNameLabel = new Label("Name : ");
        GridPane.setConstraints(planeNameLabel, 0, 0);
        planeNameLabel.setPrefWidth(100);
        TextField planeName = new TextField();
        planeName.setPrefWidth(300);
        planeName.setPromptText("Enter Planes Name!");
        GridPane.setConstraints(planeName, 1, 0);


        //manufacturer
        Label manufacturerLabel = new Label("Manufacturer  : ");
        GridPane.setConstraints(manufacturerLabel, 0, 1);
        TextField manufacturer = new TextField();
        manufacturer.setPromptText("Enter the Planes Manufacturer");
        GridPane.setConstraints(manufacturer, 1, 1);


        //age
        Label planeAgeLabel = new Label("Years in service  : ");
        GridPane.setConstraints(planeAgeLabel, 0, 2);
        TextField planeAge = new TextField();
        planeAge.setPromptText("Enter Planes's Age");
        GridPane.setConstraints(planeAge, 1, 2);

        planeAge.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if (!NewValue.equals("")) Integer.parseInt(NewValue);
            } catch (Exception e) {
                Popups.alert("Warning", " Value entered in Age is not a Number! ");
            }
        });


        //passengerNo
        Label passengerLabel = new Label("Passengers  : ");
        GridPane.setConstraints(passengerLabel, 0, 3);
        TextField passenger = new TextField();
        passenger.setPromptText("The Amount of Passengers the plane can fit");
        GridPane.setConstraints(passenger, 1, 3);

        passenger.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                    Integer.parseInt(NewValue);
            } catch (Exception e) {
                    Popups.alert("Warning", " Value entered in Passengers is not a Number! ");
            }
        });

        Button submit = new Button("Submit");
        GridPane.setColumnSpan(submit, 2);
        GridPane.setConstraints(submit, 0, 7);
        GridPane.setHalignment(submit, HPos.CENTER);

        //add all non role specific to the layout
        layout.getChildren().addAll(planeNameLabel, planeName, manufacturerLabel
                , manufacturer, planeAgeLabel, planeAge, passengerLabel, passenger, submit);


        //submit button
        submit.setOnAction(e -> {
            try{
                name = planeName.getText();
                age = Integer.parseInt(planeAge.getText());
                manufacturerName = manufacturer.getText();
                passengerNo = Integer.parseInt(passenger.getText());
                chiefStat =true; //just using it as a true statement for all info entered and correct.
                window.close();
            }
            catch(Exception err){
                Popups.alert("Error", "Please fill out the Fields");
            }

        });

        //Set the scene.
        Scene scene = new Scene(layout, 500, 300);
        scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();



        if(chiefStat){
                newPlane = new Plane(name, manufacturerName, passengerNo, age);
        }
        else {
                newPlane = new Plane("exitError32456", "", 0, 0); //dummy data used when window not complete
             }


        return newPlane;
    }


    //Flight Path

    public static FlightPath flightPath() {
        Plane newPlane;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle("Add New Plane");

        //Layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(30, 30, 30, 30)); //30px padding around window
        layout.setVgap(10); //spacing between cells vertical
        layout.setHgap(10); //spacing between cells horizontal

        //airportA
        Label airportALabel = new Label("Airport A : ");
        GridPane.setConstraints(airportALabel, 0, 0);
        airportALabel.setPrefWidth(100);
        TextField airportA = new TextField();
        airportA.setPrefWidth(300);
        airportA.setPromptText("Enter Planes Name!");
        GridPane.setConstraints(airportA, 1, 0);


        //Airport B
        Label airportBLabel = new Label("Airport B : ");
        GridPane.setConstraints(airportBLabel, 0, 1);
        TextField airportB = new TextField();
        airportB.setPromptText("Enter the second Airport");
        GridPane.setConstraints(airportB, 1, 1);


        //path time
        Label timeTakeLabel = new Label("Path Time : ");
        GridPane.setConstraints(timeTakeLabel, 0, 2);
        TextField timeTake = new TextField();
        timeTake.setPromptText("Enter how long the flight will last");
        GridPane.setConstraints(timeTake, 1, 2);

        timeTake.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if (!NewValue.equals("")) Double.parseDouble(NewValue);
            } catch (Exception e) {
                Popups.alert("Warning", " Value entered in Path Time is not a Number! ");
            }
        });

        Button submit = new Button("Submit");
        GridPane.setColumnSpan(submit, 2);
        GridPane.setConstraints(submit, 0, 7);
        GridPane.setHalignment(submit, HPos.CENTER);

        //add all non role specific to the layout
        layout.getChildren().addAll(airportALabel, airportA, airportBLabel, airportB, timeTakeLabel, timeTake, submit);


        //submit button
        submit.setOnAction(e -> {
            try{
                airportAout = airportA.getText();
                airportBout = airportB.getText();
                pathTimeout = Double.parseDouble(timeTake.getText());
                chiefStat =true; //just using it as a true statement for all info entered and correct.
                window.close();
            }
            catch(Exception err){
                Popups.alert("Error", "Please fill out the Fields");
            }

        });

        //Set the scene.
        Scene scene = new Scene(layout, 500, 300);
        scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        FlightPath newPath;
        if(chiefStat){
            newPath = new FlightPath(airportAout, airportBout, pathTimeout);
        }
        else {
            newPath = new FlightPath("exitError234556", "", 0); //dummy data used when window not complete
        }


        return newPath;
    }

}
