package saveFile;

import aguabit.scenecontrollers.DatabaseConnection;
import aguabit.scenecontrollers.MeasureScreenController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

//different package that will probably be used for saving the settings and measurements locally
public class SaveFile{
    public static boolean menuBarSide = true;
    public static boolean darkmode = false;
    private static final String pathToDocumentsFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private static final String pathForMeasurements = pathToDocumentsFolder+"/AguaBit/measurements/";
    private static final String pathForSettings = pathToDocumentsFolder + "/AguaBit/settings/";
    private static final String measurementQuery = "INSERT into measurement(user_id, measurement_name, measurement_location, slot1Type, slot2Type, slot3Type, slot1Value, slot2Value, slot3Value, date ) VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static void saveMeasurementDatabase(int userID, String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String date){
        DatabaseConnection connectionNow = new DatabaseConnection();
        try (Connection connectDB = connectionNow.getDBConnection();
             PreparedStatement pstmt = connectDB.prepareStatement(measurementQuery)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, measurementName);
            pstmt.setString(3, measurementLocation);
            pstmt.setString(4, sensor1Type);
            pstmt.setString(5, sensor2Type);
            pstmt.setString(6, sensor3Type);
            pstmt.setString(7, sensor1Value);
            pstmt.setString(8, sensor2Value);
            pstmt.setString(9, sensor3Value);
            pstmt.setString(10, date);
            pstmt.executeUpdate();
        } catch (SQLException z) {
            System.out.println(z.getMessage());
        }
        System.out.println("adding measurement succesfull");
    }

    public static void saveMeasurementLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String date){

        //saves output in to a .txt file has to be linked to measure screen controller.
        try {
            // connected to "myfile.txt"
            File measurementFile = new File(pathForMeasurements+measurementName+".txt");
            if(measurementFile.exists()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("A file with this name already exist.");
                alert.setContentText("Do you want to overwrite it?");

                if(alert.showAndWait().get()== ButtonType.OK) {
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile, date);
                }

            }else {
                measurementFile.getParentFile().mkdirs();
                try {
                    measurementFile.createNewFile();
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile, date);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println ("Error writing to file");
        }
    }

    private static void savingTheFileLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, File measurementFile, String date) throws FileNotFoundException {
        FileOutputStream out;
        PrintStream p;
        out = new FileOutputStream(measurementFile);
        p = new PrintStream(out);
        p.append(measurementName+"\n"+measurementLocation+"\n"+sensor1Type+"\n"+sensor2Type+"\n"+sensor3Type+"\n"+sensor1Value+"\n"+sensor2Value+"\n"+sensor3Value+"\n"+date);
        p.close();
        System.out.println("File successfully created");
    }

    public static void saveSettings(){
        File settingsFile = new File(pathForSettings +"settings"+ ".txt");
        settingsFile.getParentFile().mkdirs();
        try {
            settingsFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileOutputStream settingsOut;
        PrintStream settings;
        try {
            settingsOut = new FileOutputStream(settingsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        settings = new PrintStream(settingsOut);
        settings.append("menubar:"+String.valueOf(menuBarSide)+ "\n"+ "darkmode:"+ darkmode);
        settings.close();
    }

    //code for reading in the settings from the settings.txt file
    public static void readSettingsFromFile(){
        File settingsFile = new File(pathForSettings +"settings"+ ".txt");

        if(settingsFile.exists()){
            FileReader settingsFileReader = null;
            try {
                settingsFileReader = new FileReader(settingsFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader settingsReader = new BufferedReader(settingsFileReader);
            String line;

            while(true){
                try {
                    if ((line = settingsReader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(line.contains("menubar")) {
                    menuBarSide = Boolean.valueOf(line.split("\\:")[1]);
                }
                else if(line.contains("darkmode")){
                    darkmode = Boolean.valueOf(line.split("\\:")[1]);
                }
            }
        }
    }
}
