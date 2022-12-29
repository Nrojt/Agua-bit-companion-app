package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OpenMeasurementsScreenController implements Initializable {
    @FXML
    private MFXListView<String> localMeasurementsListView = new MFXListView<>();
    @FXML
    private MFXListView<String> databaseMeasurementsListView = new MFXListView<>();

    private final List<String> databaseMeasurements = new ArrayList<>();
    private final List<Integer> databaseMeasurementsMeasurementId = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //arraylist are like arrays, but the size of arraylists can be extended.
        List<String> localMeasurements = new ArrayList<>();

        //this gets all the files in the Agua:bit folder.
        File[] files = new File(SaveFile.pathForMeasurements).listFiles();

        //code for getting measurement names from the database and adding them to the listview
        if (MenuOverlayController.loginStatus) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getDBConnection();
            String measurementQuery = "SELECT measurement_id, measurement_name FROM measurement WHERE user_id = '" + MenuOverlayController.userId + "' ORDER BY date";
            Statement databaseMeasurementsStatement;
            ResultSet result;

            //getting measurements from the database and adding them to the database listview
            try {
                databaseMeasurementsStatement = connectDB.createStatement();
                result = databaseMeasurementsStatement.executeQuery(measurementQuery);

                //this code gets the amount of returned columns and puts it in a variable.
                ResultSetMetaData rsmd = result.getMetaData();
                int columnCount = rsmd.getColumnCount();
                //adding the measurement_id and the measurement_name to an array
                while (result.next()) {
                    for (int i = 2; i <= columnCount; i += 2) {
                        databaseMeasurementsMeasurementId.add(result.getInt(i-1));
                        databaseMeasurements.add(result.getString(i));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!databaseMeasurements.isEmpty()) {
                databaseMeasurementsListView.getItems().addAll(databaseMeasurements);
            } else {
                databaseMeasurementsListView.getItems().add("No saved measurements found in database");
            }
            try {
                connectDB.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            databaseMeasurementsListView.getItems().add("User not logged in");
        }

        //getting the names of the locally saved measurements and adding them to the locally saved listview
        assert files != null; //assert is a keyword that will check the condition. If the condition is not met, the error will automatically be thrown
        if (files.length > 0) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    localMeasurements.add(file.getName().split("\\.")[0]);
                }
            }
            System.out.println(localMeasurements);
            localMeasurementsListView.getItems().addAll(localMeasurements);
        } else {
            localMeasurementsListView.getItems().add("No locally saved measurements found");
        }
    }

    //Listview value is standard empty, once a measurement is selected and the open button is pressed, the window will close
    public void openSavedDatabase(){
        if(!databaseMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            int selectedItemMeasurement = -1;
            Object[] selectedItem = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if (!selectedItem[0].toString().equals("No saved measurements found in database") && !selectedItem[0].toString().equals("User not logged in")) {
                for(int i = 0; i < databaseMeasurements.size(); i++){
                    System.out.println(databaseMeasurements.get(i).toString());
                    if (databaseMeasurements.get(i).toString().equals(selectedItem[0])){
                        selectedItemMeasurement = i;
                        break;
                    }
                }
                if(selectedItemMeasurement != -1) {
                    System.out.println(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                    SaveFile.readMeasurementFromDatabase(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                    Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                    stage.close(); //closes the window
                }
            }
        } else{
            System.out.println("Nothing selected");
        }
    }

    public void openSavedLocal(){
        if(!localMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            Object[] selectedItem = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if(!selectedItem[0].toString().equals("No locally saved measurements found")) {
                String filename = selectedItem[0].toString() + ".txt";
                System.out.println(filename);
                SaveFile.readMeasurementFromFile(filename);
                Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                stage.close(); //closes the window
            }
        } else {
            System.out.println("Nothing selected");
        }
    }

    public void deleteSavedDatabase(){
        if(!databaseMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            Object[] selectedItem = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if (!selectedItem[0].toString().equals("No saved measurements found in database") && !selectedItem[0].toString().equals("User not logged in")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("You're about to delete "+ selectedItem[0].toString().split("\\. ")[1]);
                alert.setContentText("Are you sure you want to delete?");

                //Showing a prompt when the delete button is clicked, to make sure the user wants to delete
                if(alert.showAndWait().get()== ButtonType.OK) {
                    int selectedItemMeasurement = -1;
                    for(int i = 0; i < databaseMeasurements.size(); i++){
                        System.out.println(databaseMeasurements.get(i).toString());
                        if (databaseMeasurements.get(i).toString().equals(selectedItem[0])){
                            selectedItemMeasurement = i;
                            break;
                        }
                    }
                    if(selectedItemMeasurement != -1) {
                        SaveFile.deleteMeasurementFromDatabase(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                        Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                        stage.close(); //closes the window
                    }
                }
            }
        } else{
            System.out.println("Nothing selected");
        }
    }

    public void deleteSavedLocal(){
        if(!localMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            Object[] selectedItem = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if(!selectedItem[0].toString().equals("No locally saved measurements found")) {
                String filename = selectedItem[0].toString() + ".txt";

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("You're about to delete "+filename);
                alert.setContentText("Are you sure you want to delete?");

                //Showing a prompt when the delete button is clicked, to make sure the user wants to delete
                if(alert.showAndWait().get()== ButtonType.OK) {
                    System.out.println(filename);
                    SaveFile.deleteMeasurementFromFile(filename);
                    Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                    stage.close(); //closes the window
                }
            }
        } else {
            System.out.println("Nothing selected");
        }
    }
}
