package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import saveFile.SaveFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OpenMeasurementsScreenController implements Initializable {
    @FXML
    private MFXListView<String> savedMeasurementsListView = new MFXListView<>();
    @FXML
    private Label test = new Label();

    private static String currentFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!MenuOverlayController.loginStatus){
            List<String> localMeasurements = new ArrayList<String>();
            File[] files = new File(SaveFile.pathForMeasurements).listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null.
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    localMeasurements.add(file.getName());
                }
            }
            System.out.println(localMeasurements);
            savedMeasurementsListView.getItems().addAll(localMeasurements);
        }
    }
}
