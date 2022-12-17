package aguabit.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginScreenController {
    @FXML
    TextField userNameTextField;

    @FXML
    Hyperlink signupURL = new Hyperlink();
    @FXML
    Hyperlink forgotURL = new Hyperlink();
    @FXML
    public AnchorPane fxmlPane;
    private Stage stage;

    //this code runs when the loginbutton is pressed.
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        if(!userNameTextField.getText().isEmpty()) { //check to see if the login field is empty
            String username = userNameTextField.getText();
            MenuOverlayController.userName = username;
            MenuOverlayController.loginStatus = true;
            stage = (Stage) userNameTextField.getScene().getWindow();
            stage.close(); //closes the window
        }
    }


    public void onForgetURL (ActionEvent event){
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://stackoverflow.com/questions/19771836/adding-and-opening-links-on-a-jbutton"));
            }
            catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            }
    }
    public void onCreateURL (ActionEvent event){
        try {
            screenSwitcher("SignUpScreen.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //this code is here so multiple screens can be implemented in this separate window, for example a register account screen. These screens still have to be made.
    public void screenSwitcher(String fxmlFile) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
        fxmlPane.getChildren().setAll(pane);
    }
}