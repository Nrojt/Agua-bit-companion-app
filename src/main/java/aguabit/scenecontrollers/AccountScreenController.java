package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountScreenController implements Initializable {
    @FXML
    private Label userNameLabel;

    //setting all the labels
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameLabel.setText(MenuOverlayController.userName);
        //TODO make the labels update with the user information
        //TODO adding the ability to change user information in the database
    }
}
