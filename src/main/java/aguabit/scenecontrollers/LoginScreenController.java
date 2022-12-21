package aguabit.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;


public class LoginScreenController {
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
    private Stage stage;

    //this code runs when the loginbutton is pressed.
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        if(!emailTextField.getText().isEmpty()) { //check to see if the login field is empty
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String checkLoginQuery = "SELECT user_id, username FROM user WHERE email = '"+ email +"' AND password = '"+ password + "'";

            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getDBConnection();
            Statement loginStatement = null;
            ResultSet result;

            try{
                loginStatement = connectDB.createStatement();
                result = loginStatement.executeQuery(checkLoginQuery);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnCount = rsmd.getColumnCount();
                if(result.getString(1) != null && result.getString(2) != null){
                    MenuOverlayController.userId = result.getInt(1);
                    MenuOverlayController.userName = result.getString(2);
                    MenuOverlayController.loginStatus = true;
                    connectDB.close();
                    stage = (Stage) emailTextField.getScene().getWindow();
                    stage.close(); //closes the window
                }else {
                    System.out.println("incorect");
                }
                connectDB.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //MenuOverlayController.userName = username;
            //MenuOverlayController.loginStatus = true;
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