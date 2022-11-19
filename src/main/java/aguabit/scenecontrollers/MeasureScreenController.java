package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MeasureScreenController implements Initializable {
    public static String port1 = "";
    public static String port2 = "";
    public static String port3 = "";

    public static Thread measureThread;

    @FXML
    private Label sensor1Type = new Label();
    @FXML
    private Label sensor2Type = new Label();
    @FXML
    private Label sensor3Type = new Label();

    @FXML
    private Label sensor1Value = new Label();
    @FXML
    private Label sensor2Value = new Label();
    @FXML
    private Label sensor3Value = new Label();

    @FXML
    private Label sensor1Indication = new Label();
    @FXML
    private Label sensor2Indication = new Label();
    @FXML
    private Label sensor3Indication = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sensor1Type.setText(ConnectScreenController.slot1Type);
        sensor2Type.setText(ConnectScreenController.slot2Type);
        sensor3Type.setText(ConnectScreenController.slot3Type);
    }

    public void printComs(ActionEvent event) throws IOException {
        measureThread = new Thread(this::printComsActual);
        measureThread.start();
    }

    private void printComsActual(){
        port1 = "";
        port2 = "";
        port3 = "";
        SerialPort microBit = SerialPort.getCommPorts()[0];
        microBit.openPort();
        microBit.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        microBit.setBaudRate(115200);
        OutputStream sendToMicroBit = microBit.getOutputStream();
        InputStream readFromMicroBit = microBit.getInputStream();

        try {
            sendToMicroBit.write('M');

            try{
                Thread.sleep(200);
            } catch (InterruptedException ignored){}


            for(int i = 0; i < 5; i++){
                port1 += (char) readFromMicroBit.read();
            }


            for(int i = 0; i < 5; i++){
                port2 +=(char) readFromMicroBit.read();
            }


            for(int i = 0; i < 5; i++){
                port3 +=(char) readFromMicroBit.read();
            }
        } catch (SerialPortIOException ignored){} catch (Exception e) {e.printStackTrace();}
        System.out.println(port1+"\n"+port2+"\n"+port3);
        microBit.closePort();
        Platform.runLater(() -> {
            if(!port1.equals("EMPTY")){
                sensor1Value.setText(port1);
            } else {sensor1Value.setText("Empty");}
            if(!port2.equals("EMPTY")){
                sensor2Value.setText(port2);
            } else {sensor2Value.setText("Empty");}
            if(!port3.equals("EMPTY")){
                sensor3Value.setText(port3);
            } else {sensor3Value.setText("Empty");}
        });
    }

}