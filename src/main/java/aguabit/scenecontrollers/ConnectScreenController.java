package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.SerialPort;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import com.fazecast.jSerialComm.*;

public class ConnectScreenController implements Initializable {
    //variables for selecting the different slots
    public static String slot1Type = "Empty";
    public static String slot2Type = "Empty";
    public static String slot3Type = "Empty";

    @FXML
    public ChoiceBox<String> slot1SensorChoice = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot2SensorChoice = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot3SensorChoice = new ChoiceBox();
    @FXML
    private Label informationLabel = new Label();

    private String slot1Send = "AB";
    private String slot2Send = "AB";
    private String slot3Send = "AB";


    //Choiceboxes need their choices to be defined in an override
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slot1SensorChoice.getItems().addAll("PH-Sensor", "Temperature sensor", "Empty");
        slot2SensorChoice.getItems().addAll("PH-Sensor", "Temperature sensor", "Empty");
        slot3SensorChoice.getItems().addAll("PH-Sensor", "Temperature sensor",  "Empty");
    }

    //SendToProduct makes things ready to send the selected sensors to the microbit over usb
    public void sendToProduct(ActionEvent action) {
        if (slot1SensorChoice.getSelectionModel().isEmpty() || slot2SensorChoice.getSelectionModel().isEmpty() || slot3SensorChoice.getSelectionModel().isEmpty()){
            informationLabel.setText("Please make a selection for each slot");
        } else {
            //this checks if the Microbit is detected or not
            if (MenuOverlayController.isAguabitConnected) {
                slot1Type = slot1SensorChoice.getValue();
                slot2Type = slot2SensorChoice.getValue();
                slot3Type = slot3SensorChoice.getValue();

                //switch cases are like if statements
                switch (slot1SensorChoice.getValue()) {
                    case "PH-Sensor" -> slot1Send = "PH";
                    case "Temperature sensor" -> slot1Send = "TP";
                    case "Empty" -> slot1Send = "EM";
                }

                switch (slot2SensorChoice.getValue()) {
                    case "PH-Sensor" -> slot2Send = "PH";
                    case "Temperature sensor" -> slot2Send = "TP";
                    case "Empty" -> slot2Send = "EM";
                }

                switch (slot3SensorChoice.getValue()) {
                    case "PH-Sensor" -> slot3Send = "PH";
                    case "Temperature sensor" -> slot3Send = "TP";
                    case "Empty" -> slot3Send = "EM";
                }

                //this makes a new thread so the application does not lock while sending the slot choises to the microbit
                Thread connectThread = new Thread(this::sendToMicroBit);
                connectThread.start();

                informationLabel.setText("Slot choices have been sent to the Agua:bit");
            } else{
                informationLabel.setText("Please connect the Agua:bit");
            }
        }
    }

    //this code actually sends the selected sensors to the microbit
    private void sendToMicroBit(){
        //code for setting up a connection with the microbit over usb
        SerialPort microBit = SerialPort.getCommPorts()[0];
        microBit.openPort();
        microBit.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        microBit.setBaudRate(115200);
        OutputStream sendToMicroBit = microBit.getOutputStream();

        //code for sending the slot choices over usb as chars so the microbit can read them.
        try {
                sendToMicroBit.write('C');
                for (int i = 0; i < slot1Send.length(); i++) {
                    sendToMicroBit.write(slot1Send.charAt(i));
                }
                for (int i = 0; i < slot2Send.length(); i++) {
                    sendToMicroBit.write(slot2Send.charAt(i));
                }
                for (int i = 0; i < slot3Send.length(); i++) {
                    sendToMicroBit.write(slot3Send.charAt(i));
                }
                try{
                    Thread.sleep(200);
                } catch (InterruptedException ignored){}

                try{
                    Thread.sleep(200);
                } catch (InterruptedException ignored){}
        }catch(Exception e) {e.printStackTrace();}
        microBit.closePort();
    }
}
