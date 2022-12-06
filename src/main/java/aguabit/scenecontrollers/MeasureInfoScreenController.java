package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MeasureInfoScreenController implements Initializable {
    public static int slotNumber;
    public static String sensorValue;
    public static String sensorType;

    @FXML
    Label slotLabel = new Label();
    @FXML
    Label sensorTypeLabel = new Label();
    @FXML
    TextArea informationArea = new TextArea();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slotLabel.setText("Slot "+ String.valueOf(slotNumber));
        sensorTypeLabel.setText(sensorType);
        informationArea.setText(sensorValue);

    }
}
