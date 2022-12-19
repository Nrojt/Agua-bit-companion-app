package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import saveFile.SaveFile;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private String measurementQuery = "SELECT measurement_name FROM measurement WHERE user_id = '"+ MenuOverlayController.userId +"'";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> localMeasurements = new ArrayList<String>();
        File[] files = new File(SaveFile.pathForMeasurements).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        List<String> databaseMeasurements = new ArrayList<String>();

        if(MenuOverlayController.loginStatus){
            /*
            DatabaseConnection connectionNow = new DatabaseConnection();
            try (Connection connectDB = connectionNow.getDBConnection();
                 PreparedStatement pstmt = connectDB.prepareStatement(measurementQuery)) {
            } catch (SQLException z) {
                System.out.println(z.getMessage());
            }
            System.out.println("adding measurement succesfull");

             */
        }

        if(files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    localMeasurements.add(file.getName());
                }
            }
            System.out.println(localMeasurements);
            localMeasurementsListView.getItems().addAll(localMeasurements);
        } else{
            localMeasurementsListView.getItems().add("No locally saved measurements available");
            if(MenuOverlayController.loginStatus) {
                databaseMeasurementsListView.getItems().add("No saved measurements found in database");
            } else{
                databaseMeasurementsListView.getItems().add("User not logged in");
            }
        }
    }
}
