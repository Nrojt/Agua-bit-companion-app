package aguabit.scenecontrollers;

import aguabit.helpscripts.MeasurementIndications;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import aguabit.savefile.SaveFile;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CompareMeasurementsScreenController implements Initializable {
    @FXML
    private AnchorPane fxmlpane = new AnchorPane();
    //Declaring the labels, M1 is measurement 1, M2 is measurement 2
    @FXML
    private Label m1Slot1TypeLabel = new Label();
    @FXML
    private Label m1Slot2TypeLabel = new Label();
    @FXML
    private Label m1Slot3TypeLabel = new Label();
    @FXML
    private Label m2Slot1TypeLabel = new Label();
    @FXML
    private Label m2Slot2TypeLabel = new Label();
    @FXML
    private Label m2Slot3TypeLabel = new Label();

    @FXML
    private Label m1Slot1ValueLabel = new Label();
    @FXML
    private Label m1Slot2ValueLabel = new Label();
    @FXML
    private Label m1Slot3ValueLabel = new Label();
    @FXML
    private Label m2Slot1ValueLabel = new Label();
    @FXML
    private Label m2Slot2ValueLabel = new Label();
    @FXML
    private Label m2Slot3ValueLabel = new Label();

    @FXML
    private Label m1Slot1IndicationLabel = new Label();
    @FXML
    private Label m1Slot2IndicationLabel = new Label();
    @FXML
    private Label m1Slot3IndicationLabel = new Label();
    @FXML
    private Label m2Slot1IndicationLabel = new Label();
    @FXML
    private Label m2Slot2IndicationLabel = new Label();
    @FXML
    private Label m2Slot3IndicationLabel = new Label();

    @FXML
    private Label m1NameLabel = new Label();
    @FXML
    private Label m2NameLabel = new Label();
    @FXML
    private Label m1DateLabel = new Label();
    @FXML
    private Label m2DateLabel = new Label();
    @FXML
    private Label m1LocationLabel = new Label();
    @FXML
    private Label m2LocationLabel = new Label();


    public static String m1Slot1TypeString;
    public static String m1Slot2TypeString;
    public static String m1Slot3TypeString;
    public static String m2Slot1TypeString;
    public static String m2Slot2TypeString;
    public static String m2Slot3TypeString;

    public static String m1Slot1ValueString;
    public static String m1Slot2ValueString;
    public static String m1Slot3ValueString;
    public static String m2Slot1ValueString;
    public static String m2Slot2ValueString;
    public static String m2Slot3ValueString;

    public static String m1NameString = "Measurement 1";
    public static String m1LocationString = "Location";
    public static String m1DateString = "Date";
    public static String m2NameString = "Measurement 2";
    public static String m2LocationString = "Location";
    public static String m2DateString = "Date";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m1Slot1TypeLabel.setText(m1Slot1TypeString);
        m1Slot2TypeLabel.setText(m1Slot2TypeString);
        m1Slot3TypeLabel.setText(m1Slot3TypeString);
        m2Slot1TypeLabel.setText(m2Slot1TypeString);
        m2Slot2TypeLabel.setText(m2Slot2TypeString);
        m2Slot3TypeLabel.setText(m2Slot3TypeString);

        m1Slot1ValueLabel.setText(m1Slot1ValueString);
        m1Slot2ValueLabel.setText(m1Slot2ValueString);
        m1Slot3ValueLabel.setText(m1Slot3ValueString);
        m2Slot1ValueLabel.setText(m2Slot1ValueString);
        m2Slot2ValueLabel.setText(m2Slot2ValueString);
        m2Slot3ValueLabel.setText(m2Slot3ValueString);

        m1NameLabel.setText(m1NameString);
        m2NameLabel.setText(m2NameString);
        m1DateLabel.setText(m1DateString);
        m2DateLabel.setText(m2DateString);
        m1LocationLabel.setText(m1LocationString);
        m2LocationLabel.setText(m2LocationString);

        String getM1Slot1Indication = MeasurementIndications.getMeasurementIndication(m1Slot1TypeString, m1Slot1ValueString);
        m1Slot1IndicationLabel.setText(getM1Slot1Indication.split("\\:")[0]);
        m1Slot1IndicationLabel.setTextFill(Paint.valueOf(getM1Slot1Indication.split("\\:")[1]));

        String getM1Slot2Indication = MeasurementIndications.getMeasurementIndication(m1Slot2TypeString, m1Slot2ValueString);
        m1Slot2IndicationLabel.setText(getM1Slot2Indication.split("\\:")[0]);
        m1Slot2IndicationLabel.setTextFill(Paint.valueOf(getM1Slot2Indication.split("\\:")[1]));

        String getM1Slot3Indication = MeasurementIndications.getMeasurementIndication(m1Slot3TypeString, m1Slot3ValueString);
        m1Slot3IndicationLabel.setText(getM1Slot3Indication.split("\\:")[0]);
        m1Slot3IndicationLabel.setTextFill(Paint.valueOf(getM1Slot3Indication.split("\\:")[1]));

        String getM2Slot1Indication = MeasurementIndications.getMeasurementIndication(m2Slot1TypeString, m2Slot1ValueString);
        m2Slot1IndicationLabel.setText(getM2Slot1Indication.split("\\:")[0]);
        m2Slot1IndicationLabel.setTextFill(Paint.valueOf(getM2Slot1Indication.split("\\:")[1]));

        String getM2Slot2Indication = MeasurementIndications.getMeasurementIndication(m2Slot2TypeString, m2Slot2ValueString);
        m2Slot2IndicationLabel.setText(getM2Slot2Indication.split("\\:")[0]);
        m2Slot2IndicationLabel.setTextFill(Paint.valueOf(getM2Slot2Indication.split("\\:")[1]));

        String getM2Slot3Indication = MeasurementIndications.getMeasurementIndication(m2Slot3TypeString, m2Slot3ValueString);
        m2Slot3IndicationLabel.setText(getM2Slot3Indication.split("\\:")[0]);
        m2Slot3IndicationLabel.setTextFill(Paint.valueOf(getM2Slot3Indication.split("\\:")[1]));

    }

    //code to send the correct information to slotInformationScreens()
    public void m1slot1Information() throws IOException {
        slotInformationScreens(1, m1Slot1TypeString, m1Slot1ValueString);
    }
    public void m1slot2Information() throws IOException {
        slotInformationScreens(2, m1Slot2TypeString, m1Slot2ValueString);
    }
    public void m1slot3Information() throws IOException {
        slotInformationScreens(3, m1Slot3TypeString, m1Slot3ValueString);
    }
    public void m2slot1Information() throws IOException {
        slotInformationScreens(1, m2Slot1TypeString, m2Slot1ValueString);
    }
    public void m2slot2Information() throws IOException {
        slotInformationScreens(2, m2Slot2TypeString, m2Slot2ValueString);
    }
    public void m2slot3Information() throws IOException {
        slotInformationScreens(3, m2Slot3TypeString, m2Slot3ValueString);
    }

    private void slotInformationScreens(int slot, String Type, String Value) throws IOException {
        MeasureInfoScreenController.slotNumber = slot;
        MeasureInfoScreenController.sensorValue = Value;
        MeasureInfoScreenController.sensorType = Type;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MeasureInfoScreen.fxml"));
        Parent root= fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Measurement Information");
        openStage(stage, scene);
    }

    //This opens a new stage (window)
    private void openStage(Stage stage, Scene scene) {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = null;
        if(SaveFile.theme == 0) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuLight.css")).toExternalForm();
        } else if (SaveFile.theme == 1) {
            css = Objects.requireNonNull(this.getClass().getResource("PopupMenuDark.css")).toExternalForm();
        }
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void backToOpenMeasurementsScreen(){
        try {
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("OpenMeasurementsScreen.fxml")));
            fxmlpane.getChildren().setAll(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void m1OpenLocationMap() throws IOException {
        openLocationMap(m1LocationString);
    }

    public void m2OpenLocationMap() throws IOException {
        openLocationMap(m2LocationString);
    }

    private void openLocationMap(String measurementLocationString) throws IOException {
        if(measurementLocationString.contains(",") && measurementLocationString.matches("^[0-9,.]*$")){
            LocationMapScreenController.locationMapCoordinates = measurementLocationString;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LocationMapScreen.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Measurement Location");
            openStage(stage, scene);
        }
    }
}
