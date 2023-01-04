package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class OpenMeasurementsScreenController implements Initializable {
    //declaring the fxml for the screen
    @FXML
    private AnchorPane fxmlpane = new AnchorPane();
    @FXML
    private MFXListView<String> localMeasurementsListView = new MFXListView<>();
    @FXML
    private MFXListView<String> databaseMeasurementsListView = new MFXListView<>();
    @FXML
    private Label informationLabel = new Label();

    //arraylist for storing all the database measurements, cannot be local variables cause they are used in multiple places
    private final List<String> databaseMeasurements = new ArrayList<>();
    private final List<Integer> databaseMeasurementsMeasurementId = new ArrayList<>();


    //TODO make it so the valueindication popup menu's work

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //arraylist are like arrays, but the size of arraylists can be extended.
        List<String> localMeasurements = new ArrayList<>();

        //this gets all the files in the Agua:bit folder.
        File[] files = new File(SaveFile.pathForMeasurements).listFiles();

        //making it so the user can select multiple items in each listview, to be used for comparison
        localMeasurementsListView.getSelectionModel().allowsMultipleSelection();
        databaseMeasurementsListView.getSelectionModel().allowsMultipleSelection();

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
            //checking to see if there are any measurements at all
            if (!databaseMeasurements.isEmpty()) {
                databaseMeasurementsListView.getItems().addAll(databaseMeasurements);
            } else {
                databaseMeasurementsListView.getItems().add("No saved measurements found in database");
            }
            try {
                connectDB.close(); //don't forget to close the database, otherwise it will get locked
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

        informationLabel.setText("Use cntrl+left_mouse_button to (de)select");
    }

    //Listview value is standard empty, once a measurement is selected and the open button is pressed, the window will close
    public void openSavedMeasurements(){
        if(!localMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty() && databaseMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            if(localMeasurementsListView.getSelectionModel().getSelectedValues().size()  == 1) {
                Object[] selectedItems = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
                if (!selectedItems[0].toString().equals("No locally saved measurements found")) {
                    String filename = selectedItems[0].toString() + ".txt";
                    SaveFile.readMeasurementFromFile(filename);
                    Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                    stage.close(); //closes the window
                }
            } else {
                informationLabel.setText("Too many items selected, use cntrl+left_mouse_button do (de)select");
            }
        } else if(!databaseMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty() && localMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            if(databaseMeasurementsListView.getSelectionModel().getSelectedValues().size() ==1) {
                int selectedItemMeasurement = -1;
                Object[] selectedItems = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
                if (!selectedItems[0].toString().equals("No saved measurements found in database") && !selectedItems[0].toString().equals("User not logged in")) {
                    for (int i = 0; i < databaseMeasurements.size(); i++) {
                        System.out.println(databaseMeasurements.get(i));
                        if (databaseMeasurements.get(i).equals(selectedItems[0])) {
                            selectedItemMeasurement = i;
                            break;
                        }
                    }
                    //code for getting the measurement id, there is a problem if multiple measurements have the same name
                    if (selectedItemMeasurement != -1) {
                        System.out.println(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                        SaveFile.readMeasurementFromDatabase(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                        Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
                        stage.close(); //closes the window
                    }
                }
            } else{
                informationLabel.setText("Too many items selected, use cntrl+left_mouse_button to (de)select");
            }
        } else{
            informationLabel.setText("Select one item, use cntrl+left_mouse_button to (de)select");
        }
    }

    //code for deleting (multiple) saved measurements
    public void deleteSavedMeasurements(){
        boolean delete = false;
        if(!localMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            Object[] selectedItems = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if(!selectedItems[0].toString().equals("No locally saved measurements found")) {
                for (int y = 0; y < localMeasurementsListView.getSelectionModel().getSelectedValues().size(); y++) {
                    String filename = selectedItems[y].toString() + ".txt";

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete");
                    alert.setHeaderText("You're about to delete " + filename);
                    alert.setContentText("Are you sure you want to delete?");
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));

                    //Showing a prompt when the delete button is clicked, to make sure the user wants to delete
                    if (alert.showAndWait().get() == ButtonType.OK) {
                        System.out.println(filename);
                        SaveFile.deleteMeasurementFromFile(filename);
                        delete = true;
                    }
                }
            }
        } if(!databaseMeasurementsListView.getSelectionModel().getSelectedValues().isEmpty()) {
            Object[] selectedItems = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            if (!selectedItems[0].toString().equals("No saved measurements found in database") && !selectedItems[0].toString().equals("User not logged in")) {
                for(int y = 0; y < databaseMeasurementsListView.getSelectionModel().getSelectedValues().size(); y++) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete");
                    alert.setHeaderText("You're about to delete " + selectedItems[y]);
                    alert.setContentText("Are you sure you want to delete?");
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));

                    //Showing a prompt when the delete button is clicked, to make sure the user wants to delete
                    if (alert.showAndWait().get() == ButtonType.OK) {
                        int selectedItemMeasurement = -1;
                        for (int i = 0; i < databaseMeasurements.size(); i++) {
                            System.out.println(databaseMeasurements.get(i));
                            if (databaseMeasurements.get(i).equals(selectedItems[y])) {
                                selectedItemMeasurement = i;
                                break;
                            }
                        }
                        if (selectedItemMeasurement != -1) {
                            delete = true;
                            SaveFile.deleteMeasurementFromDatabase(databaseMeasurementsMeasurementId.get(selectedItemMeasurement));
                        }
                    }
                }
            }
        } else{
            informationLabel.setText("Nothing selected, use cntrl+left_mouse_button to (de)select");
        }
        if(delete){
            Stage stage = (Stage) localMeasurementsListView.getScene().getWindow();
            stage.close(); //closes the window
        }
    }

    //for comparing locally saved measurements
    public void compareMeasurements() throws IOException {
        if(localMeasurementsListView.getSelectionModel().getSelectedValues().size() == 2) {
            Object[] selectedItems = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            String m1FileName = selectedItems[0].toString() + ".txt";
            String m2FileName = selectedItems[1].toString() + ".txt";

            SaveFile.compareMeasurementsFromFile(m1FileName, m2FileName);

            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CompareMeasurementsScreen.fxml")));
            fxmlpane.getChildren().setAll(pane);
        } else if (databaseMeasurementsListView.getSelectionModel().getSelectedValues().size() == 2) {
            Object[] selectedItems = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            int m1Measurement = -1;
            int m2Measurement = -1;
            for (int i = 0; i < databaseMeasurements.size(); i++) {
                System.out.println(databaseMeasurements.get(i));
                if (databaseMeasurements.get(i).equals(selectedItems[0])) {
                    m1Measurement = i;
                    break;
                }
            }
            for (int i = 0; i < databaseMeasurements.size(); i++) {
                System.out.println(databaseMeasurements.get(i));
                if (databaseMeasurements.get(i).equals(selectedItems[1])) {
                    m2Measurement = i;
                    break;
                }
            }
            SaveFile.compareMeasurementsFromDatabase(databaseMeasurementsMeasurementId.get(m1Measurement), databaseMeasurementsMeasurementId.get(m2Measurement));
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CompareMeasurementsScreen.fxml")));
            fxmlpane.getChildren().setAll(pane);

        } else if (localMeasurementsListView.getSelectionModel().getSelectedValues().size() == 1 && databaseMeasurementsListView.getSelectionModel().getSelectedValues().size() == 1) {
            Object[] selectedDatabaseItem = databaseMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            Object[] selectedLocalItem = localMeasurementsListView.getSelectionModel().getSelectedValues().toArray();
            String localItem = selectedLocalItem[0].toString() +".txt";
            int databaseMeasurementId = -1;

            for (int i = 0; i < databaseMeasurements.size(); i++) {
                System.out.println(databaseMeasurements.get(i));
                if (databaseMeasurements.get(i).equals(selectedDatabaseItem[0])) {
                    databaseMeasurementId = i;
                    break;
                }
            }

            SaveFile.compareMeasurementsFromFileAndDatabase(databaseMeasurementsMeasurementId.get(databaseMeasurementId), localItem);
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CompareMeasurementsScreen.fxml")));
            fxmlpane.getChildren().setAll(pane);
        } else{
            informationLabel.setText("Not enough or to many values selected, use cntrl+left_mouse_button to (de)select");
        }
    }
}
