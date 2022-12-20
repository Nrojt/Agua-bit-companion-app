package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private Label test = new Label();

    private static String currentFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //arraylist are like arrays, but the size of arraylists can be extended.
        List<String> localMeasurements = new ArrayList<String>();
        List<String> databaseMeasurements = new ArrayList<String>();
        List<Integer> databaseMeasurementID = new ArrayList<Integer>();

        //this gets all the files in the Agua:bit folder.
        File[] files = new File(SaveFile.pathForMeasurements).listFiles();

        //code for getting measurement names from the database and adding them to the listview
        if (MenuOverlayController.loginStatus) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getDBConnection();
            String measurementQuery = "SELECT measurement_id, measurement_name FROM measurement WHERE user_id = '" + MenuOverlayController.userId + "' ORDER BY DATE('date')";
            Statement databaseMeasurementsStatement = null;
            ResultSet result;

            try {
                databaseMeasurementsStatement = connectDB.createStatement();
                result = databaseMeasurementsStatement.executeQuery(measurementQuery);

                //this code gets the amount of returned columns and puts it in a variable.
                ResultSetMetaData rsmd = result.getMetaData();
                int columnCount = rsmd.getColumnCount();


                while (result.next()) {
                    for (int i = 2; i <= columnCount; i += 2) {
                        databaseMeasurements.add(result.getString(i));
                        databaseMeasurementID.add(result.getInt(i - 1));
                    }
                }
                System.out.println(databaseMeasurementID);
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

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    localMeasurements.add(file.getName().toString().split("\\.")[0]);
                }
            }
            System.out.println(localMeasurements);
            localMeasurementsListView.getItems().addAll(localMeasurements);
        } else {
            localMeasurementsListView.getItems().add("No locally saved measurements found");
        }
        localMeasurementsListView.getSelectionModel().selectionProperty().addListener(this::localMeasurementsListViewSelectionChange);
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
