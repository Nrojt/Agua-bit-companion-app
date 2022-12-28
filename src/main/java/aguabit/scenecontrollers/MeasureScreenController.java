package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MeasureScreenController implements Initializable {
    //a boolean that decides if menuUpdate() should run
    public static AtomicBoolean updateThreadRunning = new AtomicBoolean(false);
    // a boolean so the measurement button cant be spammed
    private boolean noMeasurementGoingOn = true;
    //declaring the separate threads that this screen will use
    public static Thread updateThread;
    public static Thread measureThread;

    //variables for the measurement values and slot types
    public static String port1 = "Empty";
    public static String port2 = "Empty";
    public static String port3 = "Empty";

    public static String sensor1TypeString = "Unknown";
    public static String sensor2TypeString = "Unknown";
    public static String sensor3TypeString = "Unknown";
    public static String sensor1ValueString = "Unknown";
    public static String sensor2ValueString = "Unknown";
    public static String sensor3ValueString = "Unknown";
    public static String sensor1IndicationString = "Unknown";
    public static String sensor2IndicationString = "Unknown";
    public static String sensor3IndicationString = "Unknown";


    //fxml labels for measurement values and slot types
    @FXML
    private Label informationLabel = new Label();
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

    //TODO measurement indications, use these labels
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

        //changing the text for the information label
        if(MenuOverlayController.isAguabitConnected) {
            informationLabel.setText("Click one of the buttons to start");
        }else{
            informationLabel.setText("Please connect the Agua:bit");
        }

        //starting the thread that updates the ui
        updateThreadRunning.set(true);
        updateThread = new Thread(this::menuUpdate);
        updateThread.start();
    }

    public void measurementButtonClick(){
        if(MenuOverlayController.isAguabitConnected) {
            if(noMeasurementGoingOn) {
                noMeasurementGoingOn = false; //making it so the measurement button cannot be spammed
                //starting a new thread for getting the measurements from the Microbit
                measureThread = new Thread(this::getMeasurementsFromMicrobit);
                measureThread.start();
            } else {
                informationLabel.setText("Please wait for the current measurement to finish");
            }
        } else{
            informationLabel.setText("Please connect the Agua:bit");
        }
    }
    //code for opening the saveMeasurement screen
    public void saveMeasurement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveMeasurementScreen.fxml"));
        Parent root2 = fxmlLoader.load();
        Stage stage3 = new Stage();
        Scene scene3 = new Scene(root2);
        stage3.setTitle("Save Measurement");
        stage3.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = null;
        if(SaveFile.theme == 0) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuLight.css")).toExternalForm();
        } else if (SaveFile.theme == 1) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuDark.css")).toExternalForm();
        }
        scene3.getStylesheets().add(css);
        stage3.setScene(scene3);
        stage3.setResizable(false);
        stage3.show();
    }

    //code for reading the measurements from the microbit
    private void getMeasurementsFromMicrobit() {
        port1 = "";
        port2 = "";
        port3 = "";

        String microbitInput = "";

        //opening a port for communicating with the Microbit over usb
        //System.out.println(SerialPort.getCommPorts());
        //TODO find a better way to get the serialport of the microbit, if multiple serial ports are connected this current way will cause errors
        SerialPort microBit = SerialPort.getCommPorts()[0];

        microBit.openPort();
        microBit.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        microBit.setBaudRate(115200); //the speed of communication
        OutputStream sendToMicroBit = microBit.getOutputStream();
        InputStream readFromMicroBit = microBit.getInputStream();

        //code for getting the measurement reading from the microbit
        try {
            sendToMicroBit.write('M'); //when the microbit receives 'M', it will enter the measurement state.

            //giving the microbit time to receive the 'M' and send the measurements over serial
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

            //actually reading the input, the input cannot be longer then 50 characters. This is just as a failsafe
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
        //for emptying the connection, prevents problems with doing another measurement
        microBit.flushIOBuffers();
        microBit.closePort();

        //the microbit sends the values with | in between, split seperates those again
        port1 = microbitInput.split("\\|")[0];
        port2 = microbitInput.split("\\|")[1];
        port3 = microbitInput.split("\\|")[2];


        //rounding the input depending on what type of sensor it is
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

        noMeasurementGoingOn = true; //making it so the measurement button can be pressed again

        //updating the labels in the gui, runlater so it gets updated in the gui thread instead of this thread (measureThread)
        Platform.runLater(() -> {
            sensor1ValueLabel.setText(sensor1ValueString);
            sensor2ValueLabel.setText(sensor2ValueString);
            sensor3ValueLabel.setText(sensor3ValueString);
            informationLabel.setText("Measurement succesful");
        });
    }

    //code to send the correct information to slotInformationScreens()
    public void slot1Information() throws IOException{
        slotInformationScreens(1, sensor1TypeString, sensor1ValueString);
    }
    public void slot2Information() throws IOException {

        slotInformationScreens(2, sensor2TypeString, sensor2ValueString);
    }
    public void slot3Information() throws IOException {
        slotInformationScreens(3, sensor3TypeString, sensor3ValueString);
    }

    //this code rounds doubles to the amount of int decimalPlaces
    private Double roundDoubles(Double input, int decimalPlaces){
        if (decimalPlaces < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(input); //a BigDecimal handles decimal points better than doubles, probably overkill for what we use it for
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //code for opening the information screens when clicking on the indications
    private void slotInformationScreens(int slot, String Type, String Value) throws IOException {
        System.out.println(slot + " "+ Type);
        MeasureInfoScreenController.slotNumber = slot;
        MeasureInfoScreenController.sensorValue = Value;
        MeasureInfoScreenController.sensorType = Type;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MeasureInfoScreen.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(root1);
        stage2.setTitle("Measurement Information");
        stage2.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = null;
        if(SaveFile.theme == 0) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuLight.css")).toExternalForm();
        } else if (SaveFile.theme == 1) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuDark.css")).toExternalForm();
        }
        scene2.getStylesheets().add(css);
        stage2.setScene(scene2);
        stage2.setResizable(false);
        stage2.show();
    }

    //this opens the screen that displays the saved measurements
    public void openMeasurementsButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OpenMeasurementsScreen.fxml"));
        Parent root2 = fxmlLoader.load();
        Stage stage3 = new Stage();
        Scene scene3 = new Scene(root2);
        stage3.setTitle("Open saved measurements");
        stage3.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = null;
        if(SaveFile.theme == 0) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuLight.css")).toExternalForm();
        } else if (SaveFile.theme == 1) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuDark.css")).toExternalForm();
        }
        scene3.getStylesheets().add(css);
        stage3.setScene(scene3);
        stage3.setResizable(false);
        stage3.show();
    }

    //code for updating the labels on screen
    private void menuUpdate(){
        while(updateThreadRunning.get()) {
            Platform.runLater(() -> {
                sensor1ValueLabel.setText(sensor1ValueString);
                sensor2ValueLabel.setText(sensor2ValueString);
                sensor3ValueLabel.setText(sensor3ValueString);
                sensor1TypeLabel.setText(sensor1TypeString);
                sensor2TypeLabel.setText(sensor2TypeString);
                sensor3TypeLabel.setText(sensor3TypeString);
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}