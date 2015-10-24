package com.github.bitlyons.runway;

import com.github.bitlyons.utils.Popups;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditData {
    static String name, roles, manufacturerName, airportAout, airportBout;
    static int age, daysWithCompany, passengerNo;
    static double salary, flightHour, pathTimeout;
    static boolean chiefStat, purserStat;

    public static Employee staff(Employee editEmployee) {
        name = editEmployee.getName();
        age = editEmployee.getAge();
        salary = editEmployee.getSalary();
        chiefStat = editEmployee.isChiefPurser();
        purserStat = editEmployee.isPurser();
        flightHour = editEmployee.getFlightHours();
        daysWithCompany = editEmployee.getDaysWithCompany();
        roles = editEmployee.getRole();

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //stop user from interacting with other windows
        window.setTitle("Edit Staff Member");

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
        employeeName.setText(name);
        GridPane.setConstraints(employeeName, 1, 0);

        //age
        Label employeeAgeLabel = new Label("Age  : ");
        GridPane.setConstraints(employeeAgeLabel, 0, 1);
        TextField employeeAge = new TextField();
        employeeAge.setText(Integer.toString(age));
        GridPane.setConstraints(employeeAge, 1, 1);

        employeeAge.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if(NewValue.equals("")) ; // workaround for empty text, still pops up if more text entered though
                else Integer.parseInt(NewValue);
            } catch(Exception e) {
                Popups.alert("Warning", "Value entered in Age is not a Number!");
            }
        });

        //salary
        Label employeeSalaryLabel = new Label("Salary  : ");
        GridPane.setConstraints(employeeSalaryLabel, 0, 2);
        TextField employeeSalary = new TextField();
        employeeSalary.setText(Double.toString(salary));
        GridPane.setConstraints(employeeSalary, 1, 2);

        employeeSalary.textProperty().addListener((v, oldValue, NewValue) -> {
            try {
                if(!NewValue.equals("")) Double.parseDouble(NewValue);
            } catch(Exception e) {
                Popups.alert("Warning", "Value entered in Salary is not a Number!");
            }
        });

        //role
        ComboBox<String> role = new ComboBox<>();
        Label roleLabel = new Label("Role  : ");
        GridPane.setConstraints(roleLabel, 0, 3);
        role.getItems().addAll("Pilot", "Attendant");
        role.setPromptText("Please Select the Role Of The Employee");
        role.setValue(roles);
        GridPane.setConstraints(role, 1, 3);

        Button submit = new Button("Submit");
        GridPane.setColumnSpan(submit, 2);
        GridPane.setConstraints(submit, 0, 7);
        GridPane.setHalignment(submit, HPos.CENTER);


        //pilot flightHours
        Label flightHoursLabel = new Label("Flight Hours : ");
        GridPane.setConstraints(flightHoursLabel, 0, 4);
        TextField flightHours = new TextField();
        flightHours.setText(Double.toString(flightHour));
        GridPane.setConstraints(flightHours, 1, 4);


        flightHours.textProperty().addListener((v, oldValue, Newvalue) -> {
            try {
                if(Newvalue.equalsIgnoreCase("")) ;
                else Double.parseDouble(Newvalue);
            } catch(Exception e) {
                Popups.alert("Warning", "Value entered in Flight Hours is not a Number!");
            }
        });

        //Attendant chiefStatus, purserStatus
        CheckBox purserStatus = new CheckBox("Is A Purser");
        if(purserStat) purserStatus.isSelected();
        GridPane.setConstraints(purserStatus, 1, 4);
        CheckBox chiefStatus = new CheckBox("Is Chief Purser");
        if(chiefStat) chiefStatus.isSelected();
        GridPane.setConstraints(chiefStatus, 1, 5);
        chiefStatus.setDisable(true);

        purserStatus.selectedProperty().addListener((v, oldValue, NewValue) -> {
            if(NewValue) chiefStatus.setDisable(false);
            else chiefStatus.setDisable(true);
        });


        //add all non role specific to the layout
        layout.getChildren().addAll(employeeNameLabel, employeeName, employeeAgeLabel, employeeAge,
                employeeSalaryLabel, employeeSalary, roleLabel, role, submit);

        //dup code so it will run once
        if(role.getValue().equals("Attendant")) {
            layout.getChildren().removeAll(flightHoursLabel, flightHours);
            layout.getChildren().addAll(chiefStatus, purserStatus);
        }
        if(role.getValue().equals("Pilot")) {
            layout.getChildren().removeAll(chiefStatus, purserStatus);
            layout.getChildren().addAll(flightHoursLabel, flightHours);
        }


        //listen for change in role
        role.getSelectionModel().selectedItemProperty().addListener((v, oldValue, NewValue) -> {

            if(role.getValue().equals("Attendant")) {
                layout.getChildren().removeAll(flightHoursLabel, flightHours);
                layout.getChildren().addAll(chiefStatus, purserStatus);
            }
            if(role.getValue().equals("Pilot")) {
                layout.getChildren().removeAll(chiefStatus, purserStatus);
                layout.getChildren().addAll(flightHoursLabel, flightHours);
            }
        });

        //submit button
        submit.setOnAction(e -> {
            try {
                name = employeeName.getText();
                age = Integer.parseInt(employeeAge.getText());
                salary = Double.parseDouble(employeeSalary.getText());
                //can leave these two here as the checkbox will be false
                chiefStat = chiefStatus.isSelected();
                purserStat = purserStatus.isSelected();
                window.close();
            } catch(Exception err) {
                Popups.alert("Error", "Please fill out the Fields");
            }

        });

        //Set the scene.
        Scene scene = new Scene(layout, 500, 300);
        scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

        try {
            if(role.getValue().equals("Pilot")) {
                //have to put flightHour here to avoid empty value.
                flightHour = Double.parseDouble(flightHours.getText());
                editEmployee = new Pilot(name, age, salary, flightHour);
                return editEmployee;
            } else {
                if(!purserStat) chiefStat = false; //since we disabled it, workaround to set it to false
                editEmployee = new Attendant(name, age, salary, chiefStat, purserStat);
                return editEmployee;
            }
        } catch(Exception e) {
            //added for the case where the user just closes the window
        }

        //somehow no role was selected, return a dummy Employee.
        editEmployee = new Attendant("exitError88123", 0, 0, false, false);
        return editEmployee;
    }

}
