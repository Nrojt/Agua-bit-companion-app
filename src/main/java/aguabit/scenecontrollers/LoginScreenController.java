package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;


public class LoginScreenController {
    @FXML
    MFXCheckbox rememberMeCheckBox = new MFXCheckbox();
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;

    @FXML
    Hyperlink signupURL = new Hyperlink();
    @FXML
    Hyperlink forgotURL = new Hyperlink();
    @FXML
    public AnchorPane fxmlPane;


    //this code runs when the loginbutton is pressed.
    public void onLoginButtonClick(){
        if(!emailTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) { //check to see if the login fields are not empty

            System.out.println(rememberMeCheckBox.isSelected());
            String email = emailTextField.getText();
            String password = passwordTextField.getText();

            if(loginCode(email, password) && rememberMeCheckBox.isSelected()){
                SaveFile.rememberMe(email, password);
            }

            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.close(); //closes the window
        }
    }

    //opens a website when the user clicks on the forget password button
    public void onForgetURL (){
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://stackoverflow.com/questions/19771836/adding-and-opening-links-on-a-jbutton"));
            }
            catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            }
    }

    //code for checking the input against database, aka actually logging in. Separate so remember me works
    public static boolean loginCode(String email, String password){
        //code for connecting to the database
        boolean loginSuccesful = false;
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getDBConnection();
        String checkLoginQuery = "SELECT user_id, username FROM user WHERE email = '"+ email +"' AND password = '"+ password + "'";
        Statement loginStatement;
        ResultSet result; //for getting the query result

        try{
            loginStatement = connectDB.createStatement();
            result = loginStatement.executeQuery(checkLoginQuery);
            if(result.getString(1) != null && result.getString(2) != null){
                //setting the variables in MenuOverlayController to the information from the database
                MenuOverlayController.userId = result.getInt(1);
                MenuOverlayController.userName = result.getString(2);
                MenuOverlayController.loginStatus = true;
                loginSuccesful = true;
            }else{
                System.out.println("incorect");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connectDB.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //TODO make an information label that notifies the user if the entered information is wrong
        return loginSuccesful;
    }

    //when the user clicks on the signup button
    public void onSignUpURL (){
        try {
            screenSwitcher("SignUpScreen.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //this code is here so multiple screens can be implemented in this separate window, like the signup screen.
    public void screenSwitcher(String fxmlFile) throws IOException{
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        fxmlPane.getChildren().setAll(pane);
    }
}