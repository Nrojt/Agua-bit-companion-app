package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Label userNameLabel;

    public MainScreenController() {
        //okay so this gets run every time a screen loads
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameLabel.setText("Hello "+ MenuOverlayController.userName);
    }
    //TODO give the main screen functionality
}
