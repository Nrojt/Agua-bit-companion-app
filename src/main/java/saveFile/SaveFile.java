package saveFile;

import aguabit.scenecontrollers.DatabaseConnection;
import aguabit.scenecontrollers.LoginScreenController;
import aguabit.scenecontrollers.MeasureScreenController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;
import java.io.*;
import java.sql.*;

//Most of the saving to Aguabit folder and the database is done in this class.
public class SaveFile {
    public static boolean menuBarSide = true;
    public static int theme = 0;
    private static final String pathToDocumentsFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    public static final String pathForMeasurements = pathToDocumentsFolder + "/AguaBit/measurements/";
    private static final String pathForSettings = pathToDocumentsFolder + "/AguaBit/settings/";
    private static final String pathForRememberMe = pathToDocumentsFolder + "/AguaBit/account/";
    private static final String measurementQuery = "INSERT into measurement(user_id, measurement_name, measurement_location, slot1Type, slot2Type, slot3Type, slot1Value, slot2Value, slot3Value, date ) VALUES (?,?,?,?,?,?,?,?,?,?)";

    //TODO replace the creating files with java filestream.io, way easier and less code

    //code for saving the measurements to the database
    public static void saveMeasurementDatabase(int userID, String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String date) {
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
        System.out.println("Adding measurement successful");
    }

    //saves measurements in to a .txt file in the AguaBit folder (in documents folder)
    public static void saveMeasurementLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String date) {
        try {
            File measurementFile = new File(pathForMeasurements + measurementName + ".txt");
            if (measurementFile.exists()) {
                //adding a check to see if a file already exists and if the user wants to overwrite it if it does exist
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("A file with this name already exist.");
                alert.setContentText("Do you want to overwrite it?");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile, date);
                }

            } else {
                //mkdir creates the required directories if they don't exist
                measurementFile.getParentFile().mkdirs();
                try {
                    //creating the measurement file if it doesn't exist yet
                    measurementFile.createNewFile();
                    savingTheFileLocal(measurementName, measurementLocation, sensor1Type, sensor2Type, sensor3Type, sensor1Value, sensor2Value, sensor3Value, measurementFile, date);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error writing to file");
        }
    }

    //The code for actually saving the file to the AguaBit folder, separate because it needs to be called multiple times
    private static void savingTheFileLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, File measurementFile, String date) throws FileNotFoundException {
        FileOutputStream out;
        PrintStream p;
        out = new FileOutputStream(measurementFile);
        p = new PrintStream(out);
        p.append("name:").append(measurementName).append("\nlocation:").append(measurementLocation).append("\nsensor1Type:").append(sensor1Type).append("\nsensor2Type:").append(sensor2Type).append("\nsensor3Type:").append(sensor3Type).append("\nsensor1Value:").append(sensor1Value).append("\nsensor2Value:").append(sensor2Value).append("\nsensor3Value:").append(sensor3Value).append("\ndate:").append(date);
        p.close();
        System.out.println("File successfully created");
    }

    //code for saving the settings to the AguaBit folder
    public static void saveSettings() {
        File settingsFile = new File(pathForSettings + "settings" + ".txt");
        settingsFile.getParentFile().mkdirs(); //creating parent directories if they don't exist yet
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
        settings.append("menubar:").append(String.valueOf(menuBarSide)).append("\n").append("theme:").append(String.valueOf(theme));
        settings.close();
    }

    //code for reading in the settings from the settings.txt file
    public static void readSettingsFromFile() {
        File settingsFile = new File(pathForSettings + "settings" + ".txt");

        //this gets called on startup in main.java , has a check to see if a settings file exist, otherwise just does nothing.
        if (settingsFile.exists()) {
            FileReader settingsFileReader;
            try {
                settingsFileReader = new FileReader(settingsFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader settingsReader = new BufferedReader(settingsFileReader);
            String line;

            while (true) {
                try {
                    if ((line = settingsReader.readLine()) == null) break; //if the next line is empty, break the loop
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //the settings are saved as settingsname:value, split separates them again
                if (line.contains("menubar")) { //could also use line.split("\\:")[0] in stead of contains
                    menuBarSide = Boolean.parseBoolean(line.split("\\:")[1]);
                } else if (line.contains("theme")) {
                    theme = Integer.parseInt(line.split("\\:")[1]);
                }
            }
        }
    }

    //code for reading in the locally saved measurements, code pretty simila to readSettingsFromFile
    public static void readMeasurementFromFile(String filename) {
        File measurementFile = new File(pathForMeasurements + filename);

        if (measurementFile.exists()) {
            FileReader measurementFileReader;
            try {
                measurementFileReader = new FileReader(measurementFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader measurementReader = new BufferedReader(measurementFileReader);
            String line;

            while (true) {
                try {
                    if ((line = measurementReader.readLine()) == null)
                        break; //if the next line is empty, break the loop
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (line.contains("sensor1Type")) {
                    MeasureScreenController.sensor1TypeString = String.valueOf(line.split("\\:")[1]);

                } else if (line.contains("sensor2Type")) {
                    MeasureScreenController.sensor2TypeString = String.valueOf(line.split("\\:")[1]);
                } else if (line.contains("sensor3Type")) {
                    MeasureScreenController.sensor3TypeString = String.valueOf(line.split("\\:")[1]);
                } else if (line.contains("sensor1Value")) {
                    MeasureScreenController.sensor1ValueString = String.valueOf(line.split("\\:")[1]);
                } else if (line.contains("sensor2Value")) {
                    MeasureScreenController.sensor2ValueString = String.valueOf(line.split("\\:")[1]);
                } else if (line.contains("sensor3Value")) {
                    MeasureScreenController.sensor3ValueString = String.valueOf(line.split("\\:")[1]);
                }
            }
        }
    }

    //code for getting the measurements from the database
    public static void readMeasurementFromDatabase(int measurementid) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getDBConnection();
        String readMeasurementQuery = "SELECT slot1Type, slot2Type, slot3Type, slot1Value, slot2Value, slot3Value FROM measurement WHERE measurement_id = '" + measurementid + "'";
        Statement databaseMeasurementsStatement;
        ResultSet result;

        //ResultSet starts counting at 1 instead of 0
        try {
            databaseMeasurementsStatement = connectDB.createStatement();
            result = databaseMeasurementsStatement.executeQuery(readMeasurementQuery);
            MeasureScreenController.sensor1TypeString = result.getString(1);
            MeasureScreenController.sensor2TypeString = result.getString(2);
            MeasureScreenController.sensor3TypeString = result.getString(3);
            MeasureScreenController.sensor1ValueString = result.getString(4);
            MeasureScreenController.sensor2ValueString = result.getString(5);
            MeasureScreenController.sensor3ValueString = result.getString(6);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //code for saving the email and password to a textfile in the document folder when the remember me button is checked
    public static void rememberMe(String email, String password) {
        System.out.println("remember me");
        File rememberMeFile = new File(pathForRememberMe + "account" + ".txt");
        rememberMeFile.getParentFile().mkdirs(); //creating parent directories if they don't exist yet
        try {
            rememberMeFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileOutputStream rememberMeOut;
        PrintStream account;
        try {
            rememberMeOut = new FileOutputStream(rememberMeFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        account = new PrintStream(rememberMeOut);
        account.append("email:").append(email).append("\n").append("password:").append(password);
        account.close();
    }

    public static void rememberMeLogin() {
        File rememberMeFile = new File(pathForRememberMe + "account" + ".txt");

        if (rememberMeFile.exists()) {
            FileReader rememberMeFileReader;
            try {
                rememberMeFileReader = new FileReader(rememberMeFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader measurementReader = new BufferedReader(rememberMeFileReader);
            String line;
            String email = "";
            String password = "";

            while (true) {
                try {
                    if ((line = measurementReader.readLine()) == null)
                        break; //if the next line is empty, break the loop
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (line.contains("email")) {
                    email= String.valueOf(line.split("\\:")[1]);

                } else if (line.contains("password")) {
                    password = String.valueOf(line.split("\\:")[1]);
                }
            }
            LoginScreenController.loginCode(email, password);
        }

    }
}
