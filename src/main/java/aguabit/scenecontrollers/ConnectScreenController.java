package aguabit.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectScreenController implements Initializable {
    @FXML
    public ChoiceBox<String> slot1Sensor = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot2Sensor = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot3Sensor = new ChoiceBox();
    @FXML
    private Label informationLabel = new Label();

    //Choiceboxes need their choices to be defined in the override
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slot1Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot2Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot3Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
    }

    //this is going to show a confirmation to the user that the information about the connected sensors is sucesfully sent to the microbit
    public void sendToProduct(ActionEvent action) {
        System.out.println(slot1Sensor.getValue()+"\n"+slot2Sensor.getValue()+"\n"+slot3Sensor.getValue());
        if (slot1Sensor.getValue().isEmpty() || slot2Sensor.getValue().isEmpty() || slot3Sensor.getValue().isEmpty()){
            informationLabel.setText("Please select a sensor");
        } else {informationLabel.setText("Information sent to product");}
    }
}
