package com.github.bitlyons.runway;

import com.github.bitlyons.utils.FileManagment;
import com.github.bitlyons.utils.Popups;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {

    //arraylists used to hold the database files
    ArrayList<Employee> employeeDb = new ArrayList<>();
    ArrayList<Plane> planeDb = new ArrayList<>();
    ArrayList<FlightPath> flightPathDb = new ArrayList<>();

    String dirLocation, currentTable;
    Stage window;
    BorderPane layout;
    TableView<Employee> empTable;
    TableView<Plane> planeTable;
    TableView<FlightPath> flightTable;

    Plane selectedPlane;
    Employee selectedEmployee;
    FlightPath selectedPath;
    static boolean delete;
    Button deleteRecord, editRecord;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage; //make it easier to read.
        window.setTitle("Project Runway : Airline Management Software");

        /** The Top menu File menu + items */
        Menu fileMenu = new Menu("_File");

        //File -> save Database
        MenuItem saveDatabase = new MenuItem("Save Database");
        saveDatabase.setOnAction(e -> saveDb());
        fileMenu.getItems().add(saveDatabase);

        //File -> Set DB Location
        MenuItem setDbLocation = new MenuItem("Set DB Location...");
        setDbLocation.setOnAction(e -> {
            dirLocation = FileManagment.setDir(dirLocation);

            FileManagment.saveString(dirLocation, "Config/config.ini");
        });

        fileMenu.getItems().add(setDbLocation);

        // File -> Exit Program
        MenuItem exitProgram = new MenuItem("Exit Program");
        exitProgram.setOnAction(e -> exitProgram());
        fileMenu.getItems().add(exitProgram);

        /** The Top Menu help menu + items */
        Menu helpMenu = new Menu("_Help");

        //Help -> About
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> {
            String aboutText = FileManagment.loadString("Config/About");
            Popups.alert("About", aboutText);
        });
        helpMenu.getItems().add(about);

        /** Create the top menubar and populate it with the above menu items above. */
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);


        /**  Side Menu  */
        //Staff Button
        MenuButton staffButton = new MenuButton("Staff");
        MenuItem addNewStaff = new MenuItem("Add New Staff Member");
        addNewStaff.setOnAction(e -> {
            Employee newStaff = AddNew.staff();
            if(newStaff.getName().equalsIgnoreCase("exitError88123")) ;
            else {
                employeeDb.add(newStaff);
                listEmployees(); //force main window back to employee view.
            }
        });
        MenuItem viewEditStaff = new MenuItem("View Staff Members"); //originally it was also edit, ran out of time to implement
        viewEditStaff.setOnAction(e -> listEmployees());

        staffButton.getItems().addAll(addNewStaff, viewEditStaff);
        staffButton.setPrefWidth(150); //make sure buttons are the same size and fit all the text
        staffButton.setPopupSide(Side.RIGHT); //make the menu pop to the right of the button rather than below

        //Plane Button
        MenuButton planeButton = new MenuButton("Planes");
        MenuItem addNewAircraft = new MenuItem("Add New Aircraft");
        addNewAircraft.setOnAction(e -> {
            Plane newPlane = AddNew.plane();
            if(!newPlane.getName().equalsIgnoreCase("exitError32456")) {
                planeDb.add(newPlane);
                listPlanes(); //force main window back to Planes view.
            }
        });
        MenuItem viewAircrafts = new MenuItem("View Aircraft's");
        viewAircrafts.setOnAction(e -> listPlanes());
        planeButton.getItems().addAll(addNewAircraft, viewAircrafts);
        planeButton.setPrefWidth(150);
        planeButton.setPopupSide(Side.RIGHT);

        //FlightPath Button
        MenuButton flightPathButton = new MenuButton("Flight Paths");
        MenuItem addNewFlightPAth = new MenuItem("Add New Flight Path");
        addNewFlightPAth.setOnAction(e -> {
            FlightPath newFlight = AddNew.flightPath();
            if(!newFlight.getAirportA().equalsIgnoreCase("exitError234556")) {
                flightPathDb.add(newFlight);
                listFlightPath(); //force main window back to flight Paths view.
            }
        });
        MenuItem viewAFlightPaths = new MenuItem("View Flight Paths");
        viewAFlightPaths.setOnAction(e -> listFlightPath());
        flightPathButton.getItems().addAll(addNewFlightPAth, viewAFlightPaths);
        flightPathButton.setPrefWidth(150);
        flightPathButton.setPopupSide(Side.RIGHT);


        /** Create the Side Menu and populate it */
        VBox sideMenu = new VBox(5);
        sideMenu.setPadding(new Insets(5, 0, 0, 0));
        sideMenu.getChildren().addAll(staffButton, planeButton, flightPathButton);

        /** theme sidebar to look like buttons*/
        //was done to avoid also changing the theme of the top menu-bar
        staffButton.getStyleClass().add("side-menu-button");
        planeButton.getStyleClass().add("side-menu-button");
        flightPathButton.getStyleClass().add("side-menu-button");

        /** Create the Bottom Menu */
        HBox bottomMenu = new HBox(10);
        bottomMenu.prefWidth(600);
        Pane spacer = new Pane(); // will be used to right align the bottom buttons
        deleteRecord = new Button("Delete Record");
        editRecord = new Button("Edit Record");

        bottomMenu.setHgrow(spacer, Priority.ALWAYS);
        bottomMenu.setPadding(new Insets(10, 5, 5, 10));
        bottomMenu.getChildren().addAll(spacer, editRecord, deleteRecord);

        /** Create the layout */
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setLeft(sideMenu);
        layout.setBottom(bottomMenu);

        /** Show the main window */
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(Main.class.getResource("ProjectRunway.css").toExternalForm());
        window.setScene(scene);
        window.show();

        /** This is run when the user try's to close the program. */
        window.setOnCloseRequest(e -> {
            e.consume(); //stop java from closing the program
            exitProgram();  //now run our code instead to handle exiting the program
        });
        loadDb(); //load the Database.
    }

    //this method is what we will be using to handle closing of the program
    private void exitProgram() {
        saveDb(); //save data before exiting
        boolean closeWindow = Popups.confirm("Close", "Are you sure you want to close the program?");
        if(closeWindow) window.close();
    }


    private void saveDb() {
        boolean success;
        //old try/catch from before i used a text-file to save the location of the db files, left in just in case .
        try {                               //in the event that the config file was missing, it might need this.
            if(dirLocation == null) dirLocation = FileManagment.setDir(dirLocation);
        } catch(Exception e) {
            //alert the user that they are exiting without setting a database save location
            boolean save = Popups.confirm("Save?", "Don't save the Database?");
            if(!save) saveDb(); //Re-run this class since the user does want to save.
        }
        try {
            //Save each of the arraylists to there own file.
            success = FileManagment.save(employeeDb, dirLocation + "/employee.db");
            if(!success) throw new IOException("employee Database was not Saved");
            success = FileManagment.save(planeDb, dirLocation + "/airplane.db");
            if(!success) throw new IOException("airplane Database was not Saved");
            success = FileManagment.save(flightPathDb, dirLocation + "/flightPath.db");
            if(!success) throw new IOException("flight Path Database was not Saved");
        } catch(Exception e) {
            boolean saveCheck = Popups.confirm("Error Saving", "An error occurred while saving.\n\nThis is most likely " +
                    "caused by the  directory that is set as the save location no longer existing,\n or incorrect" +
                    " permissions to save there.\n\nTo avoid data loss, do you wish to use the default location which is " +
                    "'./Database' located with\ninside the folder this program is run from?");
            //Save each of the arraylists to there own file.
            if(saveCheck) {
                try {
                    success = FileManagment.save(employeeDb, "Database/employee.db");
                    if(!success) throw new IOException("employee Database was not Saved");
                    success = FileManagment.save(planeDb, "Database/airplane.db");
                    if(!success) throw new IOException("airplane Database was not Saved");
                    success = FileManagment.save(flightPathDb, "Database/flightPath.db");
                    if(!success) throw new IOException("flight Path Database was not Saved");
                } catch(Exception f) {
                    System.out.println("Saved Failed : " + f.getMessage()); //output to terminal for testing
                    Popups.alert("Error", "Save Failed");
                }
            }
        }
    }


    private void loadDb() {
        if(dirLocation == null) {
            dirLocation = FileManagment.loadString("Config/config.ini");

            if(dirLocation.equals("Error")) {
                Popups.alert("No Database Found", "Database is empty");
                System.out.println(dirLocation);
            }
        }
        try {
            employeeDb = FileManagment.loadArraylist(dirLocation + "/employee.db");
            planeDb = FileManagment.loadArraylist(dirLocation + "/airplane.db");
            flightPathDb = FileManagment.loadArraylist(dirLocation + "/flightPath.db");
        } catch(Exception e) {
            try {
                employeeDb = FileManagment.loadArraylist("Database/employee.db");
                planeDb = FileManagment.loadArraylist("Database/airplane.db");
                flightPathDb = FileManagment.loadArraylist("Database/flightPath.db");
            } catch(Exception f) {
                Popups.alert("Error", "Load Location not found!");
            }
        }
    }

    private void listEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList(employeeDb);

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(250);

        TableColumn<Employee, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Employee, String> workedColumn = new TableColumn<>("Worked");
        workedColumn.setCellValueFactory(new PropertyValueFactory<>("daysWithCompany"));

        TableColumn<Employee, String> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setMinWidth(100);

        TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        empTable = new TableView<>();
        empTable.setItems(employees);
        empTable.getColumns().addAll(nameColumn, ageColumn, workedColumn, salaryColumn, roleColumn);
        layout.setCenter(empTable);

        currentTable = "empTable";
        empTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            selectedEmployee = (Employee) newValue;
            empTable.getFocusModel().getFocusedCell();
            deleteRecord();
            editRecord();
        });
    }

    private void listPlanes() {
        ObservableList<Plane> plane = FXCollections.observableArrayList(planeDb);

        //name, manufacturer passangerNo, yearsInService
        TableColumn<Plane, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(150);

        TableColumn<Plane, String> manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

        TableColumn<Plane, String> passengerNoColumn = new TableColumn<>("Passengers");
        passengerNoColumn.setCellValueFactory(new PropertyValueFactory<>("passengerNo"));

        TableColumn<Plane, String> yearsColumn = new TableColumn<>("Years In Service");
        yearsColumn.setCellValueFactory(new PropertyValueFactory<>("yearsInService"));

        planeTable = new TableView<>();
        planeTable.setItems(plane);
        planeTable.getColumns().addAll(nameColumn, manufacturerColumn, passengerNoColumn, yearsColumn);

        layout.setCenter(planeTable);
        currentTable = "planeTable";

        planeTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            selectedPlane = (Plane) newValue;
            planeTable.getFocusModel().getFocusedCell();
            deleteRecord();
            editRecord();
        });
    }

    private void listFlightPath() {
        ObservableList<FlightPath> flights = FXCollections.observableArrayList(flightPathDb);

        //airportA, airportB, flightTime
        TableColumn<FlightPath, String> airportAName = new TableColumn<>("From");
        airportAName.setCellValueFactory(new PropertyValueFactory<>("airportA"));
        airportAName.setMinWidth(252);

        TableColumn<FlightPath, String> airportBName = new TableColumn<>("To");
        airportBName.setCellValueFactory(new PropertyValueFactory<>("airportB"));
        airportBName.setMinWidth(252);

        TableColumn<FlightPath, String> time = new TableColumn<>("Time");
        time.setCellValueFactory(new PropertyValueFactory<>("flightTime"));
        time.setMinWidth(142);

        flightTable = new TableView<>();
        flightTable.setItems(flights);
        flightTable.getColumns().addAll(airportAName, airportBName, time);
        layout.setCenter(flightTable);
        currentTable = "flightTable";

        flightTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            selectedPath = (FlightPath) newValue;
            flightTable.getFocusModel().getFocusedCell();
            deleteRecord();
            editRecord();
        });
    }

    private void deleteRecord() {
        deleteRecord.setOnAction(e -> {
            try {
                switch(currentTable) {
                    case "empTable":
                        delete = Popups.confirm("Warning", "You are about to delete : " + selectedEmployee.getName() +
                                " from the database, Continue?");
                        if(delete) employeeDb.remove(empTable.getSelectionModel().getSelectedIndex());
                        listEmployees();
                        break;
                    case "planeTable":
                        delete = Popups.confirm("Warning", "You are about to delete : " + selectedPlane.getName() +
                                " from the database, Continue?");
                        if(delete) planeDb.remove(planeTable.getSelectionModel().getSelectedIndex());
                        listPlanes();
                        break;
                    case "flightTable":
                        delete = Popups.confirm("Warning", "You are about to delete : " + selectedPath.getAirportA() +
                                " to " + selectedPath.getAirportB() + " from the database, Continue?");
                        if(delete) flightPathDb.remove(flightTable.getSelectionModel().getSelectedIndex());
                        listFlightPath();
                        break;
                    default:
                        System.out.println("currentTable not set");
                        break;
                }
            } catch(Exception f) {
                System.out.println("An error occurred"); //terminal output for testing
            }
        });
    }

    private void editRecord() {
        editRecord.setOnAction(e -> {
            try {
                switch(currentTable) {
                    case "empTable":
                        Employee editedStaff = EditData.staff(selectedEmployee);
                        if(editedStaff.getName().equalsIgnoreCase("exitError88123")) break;

                            //TODO Create a check to see if record remains unchanged

                        else {
                            employeeDb.remove(empTable.getSelectionModel().getSelectedIndex());
                            employeeDb.add(editedStaff);
                            listEmployees();
                        }
                        break;
                    case "planeTable":
                        Plane editedPlane = EditData.plane(selectedPlane);
                        if(editedPlane.getName().equalsIgnoreCase("exitError32456")) break;

                            //TODO Create a check to see if record remains unchanged

                        else {
                            planeDb.remove(planeTable.getSelectionModel().getSelectedIndex());
                            planeDb.add(editedPlane);
                            listPlanes();
                        }
                        break;
                    case "flightTable":
                        FlightPath editedPath = EditData.flightPath(selectedPath);
                        if(editedPath.getAirportA().equalsIgnoreCase("exitError234556")) break;

                            //TODO Create a check to see if record remains unchanged

                        else {
                            flightPathDb.remove(flightTable.getSelectionModel().getSelectedIndex());
                            flightPathDb.add(editedPath);
                            listFlightPath();
                        }
                        break;
                }
            } catch(Exception f) {
                System.out.println("An error occurred"); //terminal output for testing
            }
        });
    }
}