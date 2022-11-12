package aguabit.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginScreenController {
    @FXML
    TextField userNameTextField;

    @FXML
    Hyperlink signupURL = new Hyperlink();
    @FXML
    Hyperlink forgotURL = new Hyperlink();
    private Stage stage;

    //this code runs when the loginbutton is pressed.
    public void onLoginButtonCLick(ActionEvent event) throws IOException {
        if(!userNameTextField.getText().isEmpty()) { //check to see if the login field is empty
            String username = userNameTextField.getText();
            MenuOverlayController.userName = username;
            stage = (Stage) userNameTextField.getScene().getWindow();
            stage.close(); //closes the window
        }
    }

    public void onForgetURL (ActionEvent even){
        // still have to figure out a way to open a link in a browser
    }
}