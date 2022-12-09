package saveFile;

import aguabit.scenecontrollers.MeasureScreenController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;

//different package that will probably be used for saving the settings and measurements locally
public class SaveFile {
    public static boolean menuBarSide = true;
    public static boolean darkmode = false;
    private static final String pathForMeasurements = new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"/AguaBit/measurements/";
    private static final String measurementQuery = "INSERT";

    public static void saveMeasurementDatabase(int userID, String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String sensor1Indication, String sensor2Indication, String sensor3Indication){
        System.out.println(measurementQuery);
    }

    public static void saveMeasurementLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String sensor1Indication, String sensor2Indication, String sensor3Indication){

        //saves output in to a .txt file has to be linked to measure screen controller.
        FileOutputStream out;
        PrintStream p;

        try {
            // connected to "myfile.txt"
            File measurementFile = new File(pathForMeasurements+measurementName+".txt");
            if(measurementFile.exists()){
                System.out.println("");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("A file with this name already exist.");
                alert.setContentText("Do you want to overwrite it?");

                if(alert.showAndWait().get()== ButtonType.OK) {
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile);
                }

            }else {
                measurementFile.getParentFile().mkdirs();
                try {
                    measurementFile.createNewFile();
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println ("Error writing to file");
        }
    }

    private static void savingTheFileLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, File measurementFile) throws FileNotFoundException {
        FileOutputStream out;
        PrintStream p;
        out = new FileOutputStream(measurementFile);
        p = new PrintStream(out);
        p.append(measurementName+"\n"+measurementLocation+"\n"+sensor1Type+"\n"+sensor2Type+"\n"+sensor3Type+"\n"+sensor1Value+"\n"+sensor2Value+"\n"+sensor3Value+"\n"); // connected it to the port which connects to the sensor value.
        p.close();
        System.out.println("File successfully created");
    }
}
