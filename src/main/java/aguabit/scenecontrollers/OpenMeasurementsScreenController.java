package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//TODO make it so measurements can de deleted

public class OpenMeasurementsScreenController implements Initializable {
    @FXML
    private MFXListView<String> localMeasurementsListView = new MFXListView<>();
    @FXML
    private MFXListView<String> databaseMeasurementsListView = new MFXListView<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //arraylist are like arrays, but the size of arraylists can be extended.
        List<String> localMeasurements = new ArrayList<>();
        List<String> databaseMeasurements = new ArrayList<>();

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
                //TODO find out a way to not show the measurement_id but still have it in the array in case of duplicate measurement_name
                while (result.next()) {
                    for (int i = 2; i <= columnCount; i += 2) {
                        databaseMeasurements.add(result.getString(i-1)+". "+result.getString(i));
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
            databaseMeasurementsListView.getSelectionModel().selectionProperty().addListener(this::databaseMeasurementsListViewSelectionChange);
        } else {
            databaseMeasurementsListView.getItems().add("User not logged in");
        }

        //getting the names of the locally saved measurements and adding them to the locally saved listview
        if (files != null) {
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
        localMeasurementsListView.getSelectionModel().selectionProperty().addListener(this::localMeasurementsListViewSelectionChange);
    }

    //these two listeners get activated whenever a change happens to the selected value in the listview.
    //Listview value is standard empty, once a measurement is clicked on in the listview, the window will close and the values will be shown in the measurement screen
    private void databaseMeasurementsListViewSelectionChange(ObservableValue<? extends ObservableMap<Integer, String>> observableValue, ObservableMap<Integer, String> integerStringObservableMap, ObservableMap<Integer, String> integerStringObservableMap1){
        Object[] selectedItem = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
        if(!selectedItem[0].toString().equals("No saved measurements found in database")) {
            SaveFile.readMeasurementFromDatabase(Integer.parseInt(selectedItem[0].toString().split("\\. ")[0]));
            Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
            stage.close(); //closes the window
        }
    }

    private void localMeasurementsListViewSelectionChange(ObservableValue<? extends ObservableMap<Integer, String>> observableValue, ObservableMap<Integer, String> integerStringObservableMap, ObservableMap<Integer, String> integerStringObservableMap1) {
        Object[] selectedItem = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
        String filename = selectedItem[0].toString() + ".txt";

        System.out.println(filename);
        SaveFile.readMeasurementFromFile(filename);
        Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
        stage.close(); //closes the window
    }
}
