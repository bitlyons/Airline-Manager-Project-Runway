package com.github.bitlyons.runway.ui;

import javafx.scene.control.*;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class WindowObjects {
    public static MenuBar topMenu(){
        MenuBar topMenu = new MenuBar();

        //Stuff related to the File menu
        Menu fileMenu = new Menu("_File");

        //submenu used to contain all the add new items
        Menu addNew = new Menu("_Add new");
        MenuItem newEmployee = new MenuItem("Employee");
        MenuItem newPlane = new MenuItem("Plane");
        MenuItem newFlightPath = new MenuItem("Flight Path");
        addNew.getItems().addAll(newEmployee, newPlane, newFlightPath);

        //cont. of file menu
        MenuItem saveDatabase = new MenuItem("_Save Database");
        MenuItem setLocationofDB = new MenuItem("Set save location");
        MenuItem exitProgram = new MenuItem("E_xit");
        fileMenu.getItems().addAll(addNew, saveDatabase, setLocationofDB, exitProgram);

        //edit Menu Stuff
        Menu editMenu = new Menu("Edit");
        MenuItem editCurrentSelected = new MenuItem("Edit Currently Selected");
        editMenu.getItems().addAll(editCurrentSelected);

        //Help menu Stuff
        Menu helpMenu = new Menu("_Help");
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().addAll(about);

        topMenu.getMenus().addAll(fileMenu, editMenu,helpMenu);
        return topMenu;
    }
}
