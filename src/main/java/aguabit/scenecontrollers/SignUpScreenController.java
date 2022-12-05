package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SignUpScreenController implements Initializable {
    //making variables for the textfields and the button on this screen
    @FXML
    public TextField usernameTextfield = new TextField();
    @FXML
    public TextField firstnameTextfield = new TextField();
    @FXML
    public TextField lastnameTextfield = new TextField();
    @FXML
    public TextField phonenumberTextfield = new TextField();
    @FXML
    public TextField emailTextfield = new TextField();
    @FXML
    public TextField passwordTextfield = new TextField();
    @FXML
    public MFXDatePicker dobPicker = new MFXDatePicker();
    @FXML
    public MFXButton signupButton = new MFXButton();



    public void printInput(ActionEvent e){
        if(!usernameTextfield.getText().isBlank() && !passwordTextfield.getText().isBlank() && !emailTextfield.getText().isBlank()) {
            System.out.println("username " + usernameTextfield.getText());
            System.out.println("firstname " + firstnameTextfield.getText());
            System.out.println("lastname " + lastnameTextfield.getText());
            System.out.println("phone number " + phonenumberTextfield.getText());
            System.out.println("email " + emailTextfield.getText());
            System.out.println("password " + passwordTextfield.getText());
            System.out.println("date of birth " + dobPicker.getValue());
        } else{
            System.out.println("not filled");
        }

    }



    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getDBConnection();

        String connectQuery = "SELECT * FROM user";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {

                String user_id = queryOutput.getString("user_id");
                String first_name = queryOutput.getString("first_name");
                String username = queryOutput.getString("username");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
