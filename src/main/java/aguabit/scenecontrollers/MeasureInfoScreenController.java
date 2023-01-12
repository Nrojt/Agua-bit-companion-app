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
        slotLabel.setText("Slot " + slotNumber);
        sensorTypeLabel.setText(sensorType);

        String textForInformationArea = "The value of the measurement is: " + sensorValue;

        //Adding information about the value if it is a temperature value
        if (sensorType.equals("Temperature sensor")) {
            if (Double.parseDouble(sensorValue) >= 20 && Double.parseDouble(sensorValue) <= 70) {
                textForInformationArea += ("\n" + "If your water is between 20 and 45 degrees, the bacteria can form and multiply faster. Between 20 and 45 degrees is the optimum temperature for bacteria. With water above 45 degrees, it wil get tougher for bacteria to form.");
            } else if (Double.parseDouble(sensorValue) >= 0 && Double.parseDouble(sensorValue) <= 20) {
                textForInformationArea += ("\n" + "If your water is too cold, your brain gets a signal to raise your body temperature. It is also not good for your stomach and respiratory tract, because cold water allows the fat in your body to harden faster. Furthermore, your digestion is slowed down and cold water causes your blood vessels to contract. The optimal temperature of water is between 10 and 20 degrees.");
            } else if (Double.parseDouble(sensorValue) >= 70 && Double.parseDouble(sensorValue) <= 100) {
                textForInformationArea += ("\n" + "At temperatures above 70-75 degrees, the bacteria die. This makes the water safer to ingest, but the vitamins and minerals can evaporate or reduce in substance.");
            }
        }
        //Adding information about the value if it is a ph value
        else if (sensorType.equals("PH-Value")) {
            if (Double.parseDouble(sensorValue) >= 0 && Double.parseDouble(sensorValue) <= 6.5) {
                textForInformationArea += ("\n" + "Below the PH value of 6,5 the water will be too acidic to consume. It is also bad for your teeth and body to drink water below a pH value of 6,5. to solve this you can add a few PH drops or tablets to neutralize the acid.");
            } else if (Double.parseDouble(sensorValue) >= 6.5 && Double.parseDouble(sensorValue) <= 8) {
                textForInformationArea += ("\n" + "This is the appropriate pH value of water that you can consume without any consequence.");
            } else if (Double.parseDouble(sensorValue) >= 8 && Double.parseDouble(sensorValue) <= 14) {
                textForInformationArea += ("\n" + "Above the PH value of 8 the water will contain more alkaline. This can cause irritation and it can dry out your skin. As a remedy for water above the PH value of 8 you can put some citric acid (a couple squeezes of lemon) in your water, which will lower the pH value of the water before you drink it.");
            }
        }
        //Showing something if the sensor is empty
        else{
            textForInformationArea += ("\n"+ "This is because no sensor was connected and/or selected for this slot.");
        }

            informationArea.setText(textForInformationArea);
        }
    }
