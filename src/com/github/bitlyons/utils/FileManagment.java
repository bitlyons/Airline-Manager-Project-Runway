package com.github.bitlyons.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by : Brendan Lyons (20066143)
 * About: contains methods relating to file management, Saving, Loading and selecting directory's.
 */
public class FileManagment {

    public static boolean save(Object objectName, String saveLocation)
    {
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveLocation));
            outputStream.writeObject(objectName);
            outputStream.close();
        }
        catch (IOException e)
        {
            System.err.println("IOException: " + e.getMessage() ); //output to terminal for debugging
            return false;
            //removing this line for now.
            //Popups.alert("Warning","Save Error\nFile/location Not found"); //popup for end user.
        }
        return true;
    }


    public static void saveString(String stringin, String saveLocation){
        FileWriter save;
        if(stringin !=null){
        try {
            File saveHere = new File(saveLocation);
            save = new FileWriter(saveHere);
            save.write(stringin);
            save.close();

        } catch (IOException e) {
            System.out.println("Error");
        }}
        else{System.out.println("empty");} //was used to find an issue were it was writing an empty string.
    }


    public static String loadString(String loadLocation) {
        String string;
        try {
            FileReader read = new FileReader(loadLocation);
            string = "";
            int i;
            while((i = read.read())!=-1){
                char input = (char)i;
                string = string + input;
            }


        } catch (IOException e) {
            string = "Error";
            System.out.println("File Not Found");
        }

        return string;
    }


    public static ArrayList loadArraylist(String loadLocation) {
        ArrayList<Object> arrayList = new ArrayList<>();
        try
        {
            FileInputStream fileIn = new FileInputStream(loadLocation);
            ObjectInputStream streamIn = new  ObjectInputStream(fileIn);
            arrayList = (ArrayList<Object>) streamIn.readObject();
            streamIn.close();
            fileIn.close();
        }
        catch(IOException e)
        {
            System.out.println("File Not Found");
        }

        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }


    public static String setDir(String dirLocationIn){ //taking in a string to avoid a possible null return.
        String dirLocation = dirLocationIn;
        Stage window = new Stage();

        try {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select Database Save Location");
            dirLocation = dirChooser.showDialog(window).toString();
        }
        catch (Exception e){
            System.out.println("error");
            Popups.alert("No Directory Set", "Please Select the Directory\n that you wish to use");
        }
        return dirLocation;
    }
}
