package com.github.bitlyons.runway;

import com.github.bitlyons.utils.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;


public class Main extends Application {

    //arraylists used to hold the database files
    ArrayList<Employee> employeeDb = new ArrayList<>();
    ArrayList<Plane> planeDb = new ArrayList<>();
    ArrayList<FlightPath> flightPathDb = new ArrayList<>();

    String dirLocation; //This will be used to set location of the data files
    Stage window;
    BorderPane layout;
    TableView<Employee> empTable;
    TableView<Plane> planeTable;
    TableView<FlightPath> flightTable;
    //Only used to start the program, start below is called from launch.
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
            Popups.alert("About",aboutText);
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
           Employee newStaff =  AddNew.staff();
            if(newStaff.getName().equalsIgnoreCase("exitError88123")){
                System.out.println("Add new Staff was closed, not adding to arraylist"); //terminal output for testing
            }
            else{
                employeeDb.add(newStaff);
                listEmployees(); //force main window back to employee view.
            }
        });
        MenuItem viewEditStaff = new MenuItem("View Staff Members"); //originally it was also edit, ran out of time to implement
        viewEditStaff.setOnAction(e -> listEmployees());

        staffButton.getItems().addAll(addNewStaff, viewEditStaff);
        staffButton.setPrefWidth(120); //make sure buttons are the same size and fit all the text
        staffButton.setPopupSide(Side.RIGHT); //make the menu pop to the right of the button rather than below

        //Plane Button
        MenuButton planeButton = new MenuButton("Planes");
        MenuItem addNewAircraft = new MenuItem("Add New Aircraft");
        addNewAircraft.setOnAction(e ->{
                    Plane newPlane =  AddNew.plane();
                    if(!newPlane.getName().equalsIgnoreCase("exitError32456")){
                        planeDb.add(newPlane);
                        listPlanes(); //force main window back to Planes view.
                    }
        });
        MenuItem viewAircrafts = new MenuItem("View Aircraft's");
        viewAircrafts.setOnAction(f ->listPlanes());
        planeButton.getItems().addAll(addNewAircraft,viewAircrafts);
        planeButton.setPrefWidth(120);
        planeButton.setPopupSide(Side.RIGHT);

        //FlightPath Button
        MenuButton flightPathButton = new MenuButton("Flight Paths");
        MenuItem addNewFlightPAth = new MenuItem("Add New Flight Path");
        addNewFlightPAth.setOnAction(e ->{});
        MenuItem viewAFlightPaths = new MenuItem("View Flight Paths");
        viewAFlightPaths.setOnAction(e ->listFlightPath());
        flightPathButton.getItems().addAll(addNewFlightPAth,viewAFlightPaths);
        flightPathButton.setPrefWidth(120);
        flightPathButton.setPopupSide(Side.RIGHT);

        /** Create the Side Menu and populate it */
        VBox sideMenu = new VBox();
        sideMenu.getChildren().addAll(staffButton, planeButton, flightPathButton);

        /** Create the layout */
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setLeft(sideMenu);

        /** Show the main window */
        Scene scene = new Scene(layout, 800, 600);
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
        boolean closewindow = Popups.confirm("Close", "Are you sure you want to close the program?");
        if (closewindow) window.close();
    }


    private void saveDb() {
        //old try/catch from before i used a text-file to save the location of the db files, left in just in case .
        try {                               //in the event that the config file was missing, it might need this.
            if (dirLocation == null) dirLocation = FileManagment.setDir(dirLocation);
        } catch (Exception e) {
            //alert the user that they are exiting without setting a database save location
            boolean save = Popups.confirm("Save?", "Don't save the Database?");
            if (!save) saveDb(); //Re-run this class since the user does want to save.
        }
           try {
               //Save each of the arraylists to there own file.
               FileManagment.save(employeeDb, dirLocation + "/employee.db");
               FileManagment.save(planeDb, dirLocation + "/airplane.db");
               FileManagment.save(flightPathDb, dirLocation + "/flightPath.db");
           }
           catch(Exception e){
               Popups.alert("Error Saving", "An error occurred while saving, to avoid loss\n of the database, the default location will be used");
               //Save each of the arraylists to there own file.
               FileManagment.save(employeeDb, "Database/employee.db");
               FileManagment.save(planeDb, "Database/airplane.db");
               FileManagment.save(flightPathDb, "Database/flightPath.db");
           }
    }


    private void loadDb(){
        if(dirLocation == null){
            dirLocation = FileManagment.loadString("Config/config.ini");

            if (dirLocation.equals("Error")){
                Popups.alert("No Database Found", "Database is empty");
                System.out.println(dirLocation);
            }
        }
       try {
           employeeDb = FileManagment.loadArraylist(dirLocation + "/employee.db");
           planeDb = FileManagment.loadArraylist(dirLocation +"/airplane.db");
           flightPathDb = FileManagment.loadArraylist(dirLocation +"/flightPathDb");
       }
       catch(Exception e){ //in the event that the dirLocation is incorrect, try default location.
            employeeDb = FileManagment.loadArraylist("Database/employee.db");
            planeDb = FileManagment.loadArraylist("Database/airplane.db");
            flightPathDb = FileManagment.loadArraylist("Database/flightPathDb");
        }
    }

    private void listEmployees(){
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
        planeTable.getColumns().addAll(nameColumn,manufacturerColumn,passengerNoColumn,yearsColumn);

        layout.setCenter(planeTable);
    }

    private void listFlightPath(){
        ObservableList<FlightPath> flights = FXCollections.observableArrayList(flightPathDb);

        //airportA, airportB, flightTime
        TableColumn<FlightPath, String> airportAName = new TableColumn<>("From");
        airportAName.setCellValueFactory(new PropertyValueFactory<>("airportA"));

        TableColumn<FlightPath, String> airportBName = new TableColumn<>("To");
        airportBName.setCellValueFactory(new PropertyValueFactory<>("airportB"));

        flightTable = new TableView<>();
        flightTable.setItems(flights);
        flightTable.getColumns().addAll(airportAName, airportBName);
        layout.setCenter(flightTable);

    }

}