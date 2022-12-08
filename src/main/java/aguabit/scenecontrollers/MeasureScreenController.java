package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
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

    //labels for the measurement indications, these are not implemented yet.
    @FXML
    private Label sensor1Indication = new Label();
    @FXML
    private Label sensor2Indication = new Label();
    @FXML
    private Label sensor3Indication = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting the sensors to the sensors which were selected in the connect screen.
        sensor1Type.setText(ConnectScreenController.slot1Type);
        sensor2Type.setText(ConnectScreenController.slot2Type);
        sensor3Type.setText(ConnectScreenController.slot3Type);
    }

    public void printComs(ActionEvent event) throws IOException {
        if (MenuOverlayController.isAguabitConnected) {
            //starting a new thread for getting the measurements from the Microbit
            measureThread = new Thread(this::getMeasurementsFromMicrobit);
            measureThread.start();
        } else {
            System.out.println("Please connect the Agua:bit");
        }
    }

    private void getMeasurementsFromMicrobit() {
        port1 = "";
        port2 = "";
        port3 = "";

        //opening a port for communicating with the Microbit over usb
        SerialPort microBit = SerialPort.getCommPorts()[0];
        microBit.openPort();
        microBit.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        microBit.setBaudRate(115200); //the speed of communication
        OutputStream sendToMicroBit = microBit.getOutputStream();
        InputStream readFromMicroBit = microBit.getInputStream();

        //code for getting the measurement reading from the microbit
        try {
            sendToMicroBit.write('M');

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }


            for (int i = 0; i < 5; i++) {
                port1 += (char) readFromMicroBit.read();
            }


            for (int i = 0; i < 5; i++) {
                port2 += (char) readFromMicroBit.read();
            }


            for (int i = 0; i < 5; i++) {
                port3 += (char) readFromMicroBit.read();
            }
        } catch (SerialPortIOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(port1 + "\n" + port2 + "\n" + port3);
        microBit.closePort();

        //updating the labels in the gui, runlater so it gets updated in the gui thread instead of this thread (measureThread)
        Platform.runLater(() -> {
            if (!port1.equals("EMPTY")) {
                sensor1Value.setText(port1);
            } else {
                sensor1Value.setText("Empty");
            }
            if (!port2.equals("EMPTY")) {
                sensor2Value.setText(port2);
            } else {
                sensor2Value.setText("Empty");
            }
            if (!port3.equals("EMPTY")) {
                sensor3Value.setText(port3);
            } else {
                sensor3Value.setText("Empty");
            }
        });
    }

    public void slot1Information() throws IOException {
        slotInformationScreens(1, sensor1Type.getText(), sensor1Value.getText());

    }

    public void slot2Information() throws IOException {

        slotInformationScreens(2, sensor2Type.getText(), sensor2Value.getText());
    }

    public void slot3Information() throws IOException {

        slotInformationScreens(3, sensor3Type.getText(), sensor3Value.getText());
    }

    private void slotInformationScreens(int slot, String sensorType, String sensorValue) throws IOException {
        System.out.println(slot + " " + sensorType);
        MeasureInfoScreenController.slotNumber = slot;
        MeasureInfoScreenController.sensorValue = sensorValue;
        MeasureInfoScreenController.sensorType = sensorType;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MeasureInfoScreen.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(root1);
        stage2.setTitle("Measurement Information");
        stage2.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = Objects.requireNonNull(this.getClass().getResource("LoginScreen.css")).toExternalForm();
        scene2.getStylesheets().add(css);
        stage2.setScene(scene2);
        stage2.setResizable(false);
        stage2.show();
    }

    public static void main(String args[]) {

        //saves output in to a .txt file has to be linked to measure screen controller.
        FileOutputStream out;
        PrintStream p;

        try {
            // connected to "myfile.txt"
            out = new FileOutputStream("myfile.txt");
            p = new PrintStream(out);
            p.append(port1 + "\n" + port2 + "\n" + port3); // connected it to the port which connects to the sensor value.

            p.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            //System.err.println ("Error writing to file")
        }
    }
}
