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
        slotLabel.setText("Slot " + String.valueOf(slotNumber));
        sensorTypeLabel.setText(sensorType);
        informationArea.setText(sensorValue);
        if (sensorType.equals("Temperature sensor")) {
            if (Double.parseDouble(sensorValue) >= 25 && Double.parseDouble(sensorValue) <= 70) {
                informationArea.setText("If your water is between 25 and 45 degrees. The bacteria can form and multiply faster. Because this is the optimum temperature for bacteria. With water above 45 degrees, it wil get tougher for bacteria to form and the process will also be sluggish.");
            } else if (Double.parseDouble(sensorValue) >= 0 && Double.parseDouble(sensorValue) <= 25) {
                informationArea.setText("If your water is too cold, your brain gets a signal to raise your body temperature. It is also not good for your stomach and respiratory tract because cold water allows the fat in your body to harden faster, also your digestion is slowed down. Cold water also causes your blood vessels to contract. the optimal temperature of water is between 10 and 15 degrees.");
            } else if (Double.parseDouble(sensorValue) >= 70 && Double.parseDouble(sensorValue) <= 100) {
                informationArea.setText("At temperatures above 70 to 75 degrees, the bacteria die. And you can drink the water the minus point is that the vitamins and minerals can evaporate or reduce in substance.");
            }
        }

        if (sensorType.equals("PH-Value")) {
            if (Double.parseDouble(sensorValue) >= 0 && Double.parseDouble(sensorValue) <= 6.5) {
                informationArea.setText("Below the PH value of 6,5 the water will be too acidic to consume. It is also bad for your teeth and body to drink water below a pH value of 6,5. to solve this you can add a few PH drops or tablets to neutralize the acid.");
            } else if (Double.parseDouble(sensorValue) >= 6.5 && Double.parseDouble(sensorValue) <= 8) {
                informationArea.setText("this is the appropriate pH value of water that you can consume without any consequence.");
            } else if (Double.parseDouble(sensorValue) >= 8 && Double.parseDouble(sensorValue) <= 14) {
                informationArea.setText("Above the PH value of 8 the water will contain more alkaline. This can cause irritation and it can dry out your skin. As a remedy for water above the PH value of 8 you can put some citric acid (a couple squeezes of lemon) in your water. Which ultimately lowers the pH value of the water before you drink it.");
            }
        }
    }
}