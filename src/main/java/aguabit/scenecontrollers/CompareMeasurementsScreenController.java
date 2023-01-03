package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CompareMeasurementsScreenController implements Initializable {
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

    /*
    public static String m1Slot1IndicationString;
    public static String m1Slot2IndicationString;
    public static String m1Slot3IndicationString;
    public static String m2Slot1IndicationString;
    public static String m2Slot2IndicationString;
    public static String m2Slot3IndicationString;
     */

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
    }
}
