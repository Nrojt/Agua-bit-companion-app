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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MeasureScreenController implements Initializable {
    public static String port1 = "Empty";
    public static String port2 = "Empty";
    public static String port3 = "Empty";

    public static Thread measureThread;

    public static String sensor1TypeString = "Unknown";
    public static String sensor2TypeString = "Unknown";
    public static String sensor3TypeString = "Unknown";
    public static String sensor1ValueString = "Unknown";
    public static String sensor2ValueString = "Unknown";
    public static String sensor3ValueString = "Unknown";
    public static String sensor1IndicationString = "Unknown";
    public static String sensor2IndicationString = "Unknown";
    public static String sensor3IndicationString = "Unknown";


    @FXML
    private Label sensor1TypeLabel = new Label();
    @FXML
    private Label sensor2TypeLabel = new Label();
    @FXML
    private Label sensor3TypeLabel = new Label();

    @FXML
    private Label sensor1ValueLabel = new Label();
    @FXML
    private Label sensor2ValueLabel = new Label();
    @FXML
    private Label sensor3ValueLabel = new Label();

    //labels for the measurement indications, these are not implemented yet.
    @FXML
    private Label sensor1IndicationLabel = new Label();
    @FXML
    private Label sensor2IndicationLabel = new Label();
    @FXML
    private Label sensor3IndicationLabel = new Label();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting the sensors to the sensors which were selected in the connect screen.
        sensor1TypeString = ConnectScreenController.slot1Type;
        sensor2TypeString = ConnectScreenController.slot2Type;
        sensor3TypeString = ConnectScreenController.slot3Type;


        sensor1ValueLabel.setText(sensor1ValueString);
        sensor2ValueLabel.setText(sensor2ValueString);
        sensor3ValueLabel.setText(sensor3ValueString);

        sensor1TypeLabel.setText(sensor1TypeString);
        sensor2TypeLabel.setText(sensor2TypeString);
        sensor3TypeLabel.setText(sensor3TypeString);

        sensor1IndicationLabel.setText(sensor1IndicationString);
        sensor2IndicationLabel.setText(sensor2IndicationString);
        sensor3IndicationLabel.setText(sensor3IndicationString);
    }

    public void printComs(ActionEvent event) throws IOException {
        if(MenuOverlayController.isAguabitConnected) {
            //starting a new thread for getting the measurements from the Microbit
            measureThread = new Thread(this::getMeasurementsFromMicrobit);
            measureThread.start();
        } else{
            System.out.println("Please connect the Agua:bit");
        }
    }

    public void saveMeasurement(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveMeasurementScreen.fxml"));
        Parent root2 = (Parent) fxmlLoader.load();
        Stage stage3 = new Stage();
        Scene scene3 = new Scene(root2);
        stage3.setTitle("Save Measurement");
        stage3.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = Objects.requireNonNull(this.getClass().getResource("LoginScreen.css")).toExternalForm();
        scene3.getStylesheets().add(css);
        stage3.setScene(scene3);
        stage3.setResizable(false);
        stage3.show();
    }

    private void getMeasurementsFromMicrobit() {
        port1 = "";
        port2 = "";
        port3 = "";

        String microbitInput = "";

        //opening a port for communicating with the Microbit over usb
        //System.out.println(SerialPort.getCommPorts());
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
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

            for(int i = 0; i< 50; i++){
                char input = (char) readFromMicroBit.read();
                if(input == '!' || String.valueOf(input).isBlank()){
                    break;
                }else{
                    microbitInput += input;
                }
            }

        } catch (SerialPortIOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(microbitInput);
        microBit.flushIOBuffers();
        microBit.closePort();


        port1 = microbitInput.split("\\|")[0];
        port2 = microbitInput.split("\\|")[1];
        port3 = microbitInput.split("\\|")[2];

        switch(sensor1TypeString) {
            case "PH-Value":
                port1 = String.valueOf(roundDoubles((Double.parseDouble(port1)/100),2));
                break;
            case "Temperature sensor":
                port1 = String.valueOf(roundDoubles(Double.parseDouble(port1),2));
            default:
                break;
        }

        switch(sensor2TypeString) {
            case "PH-Value":
                port2 = String.valueOf(roundDoubles((Double.parseDouble(port2)/100),2));
                break;
            case "Temperature sensor":
                port2 = String.valueOf(roundDoubles(Double.parseDouble(port2),2));
            default:
                break;
        }

        switch(sensor3TypeString) {
            case "PH-Value":
                port3 = String.valueOf(roundDoubles((Double.parseDouble(port3)/100),2));
                break;
            case "Temperature sensor":
                port3 = String.valueOf(roundDoubles(Double.parseDouble(port3),2));
            default:
                break;
        }

        sensor1ValueString = port1;
        sensor2ValueString = port2;
        sensor3ValueString = port3;

        System.out.println(port1+"\n"+port2+"\n"+port3+"\n");

        //updating the labels in the gui, runlater so it gets updated in the gui thread instead of this thread (measureThread)
        Platform.runLater(() -> {
            sensor1ValueLabel.setText(sensor1ValueString);
            sensor2ValueLabel.setText(sensor2ValueString);
            sensor3ValueLabel.setText(sensor3ValueString);
        });
    }

    public void slot1Information() throws IOException{
        slotInformationScreens(1, sensor1TypeString, sensor1ValueString);
    }

    public void slot2Information() throws IOException {

        slotInformationScreens(2, sensor2TypeString, sensor2ValueString);
    }
    public void slot3Information() throws IOException {

        slotInformationScreens(3, sensor3TypeString, sensor3ValueString);
    }

    private Double roundDoubles(Double input, int decimalPlaces){
        if (decimalPlaces < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(input);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void slotInformationScreens(int slot, String Type, String Value) throws IOException {
        System.out.println(slot + " "+ Type);
        MeasureInfoScreenController.slotNumber = slot;
        MeasureInfoScreenController.sensorValue = Value;
        MeasureInfoScreenController.sensorType = Type;
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

}