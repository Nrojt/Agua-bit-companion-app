package aguabit.scenecontrollers;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import location.Location;
import saveFile.SaveFile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SaveMeasurementScreenController implements Initializable {
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

    @FXML
    private TextField measurementNameTextfield = new TextField();
    @FXML
    private TextField measurementLocationTextfield = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sensor1Type.setText(MeasureScreenController.sensor1TypeString);
        sensor2Type.setText(MeasureScreenController.sensor2TypeString);
        sensor3Type.setText(MeasureScreenController.sensor2TypeString);

        sensor1Value.setText(MeasureScreenController.sensor1ValueString);
        sensor2Value.setText(MeasureScreenController.sensor2ValueString);
        sensor3Value.setText(MeasureScreenController.sensor3ValueString);

        sensor1Indication.setText(MeasureScreenController.sensor1IndicationString);
        sensor2Indication.setText(MeasureScreenController.sensor2IndicationString);
        sensor3Indication.setText(MeasureScreenController.sensor3IndicationString);


        try {
            measurementLocationTextfield.setText((Location.getUserLocation()));

        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMeasurement(ActionEvent e){
        //code for actually saving the measurement
        if(!measurementNameTextfield.getText().isBlank()){
            if(MenuOverlayController.loginStatus) {
                System.out.println("saved online");
            } else{
                SaveFile.saveMeasurementLocal(measurementNameTextfield.getText(), measurementLocationTextfield.getText(), sensor1Type.getText(), sensor2Type.getText(), sensor3Type.getText(), sensor1Value.getText(), sensor2Value.getText(), sensor3Value.getText(), sensor1Indication.getText(), sensor2Indication.getText(), sensor3Indication.getText());
            }
            Stage stage = (Stage) measurementNameTextfield.getScene().getWindow();
            stage.close(); //closes the window
        }

    }
}
