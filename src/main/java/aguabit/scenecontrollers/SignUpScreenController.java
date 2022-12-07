package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUpScreenController{
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

    protected String querry = "INSERT INTO user(first_name, last_name, username, phonenumber, password, email, date_of_birth) VALUES(?,?,?,?,?,?,?)";;


    public void printInput(ActionEvent e){
        if(!usernameTextfield.getText().isBlank() && !passwordTextfield.getText().isBlank() && !emailTextfield.getText().isBlank()) {
            System.out.println("username " + usernameTextfield.getText());
            System.out.println("firstname " + firstnameTextfield.getText());
            System.out.println("lastname " + lastnameTextfield.getText());
            System.out.println("phone number " + phonenumberTextfield.getText());
            System.out.println("email " + emailTextfield.getText());
            System.out.println("password " + passwordTextfield.getText());
            System.out.println("date of birth " + dobPicker.getValue());
            DatabaseConnection connectionNow = new DatabaseConnection();
            //Connection connectDB = connectionNow.getDBConnection();
            try (Connection connectDB = connectionNow.getDBConnection();
                 PreparedStatement pstmt = connectDB.prepareStatement(querry)) {
                pstmt.setString(1, firstnameTextfield.getText());
                pstmt.setString(2, lastnameTextfield.getText());
                pstmt.setString(3, usernameTextfield.getText());
                pstmt.setInt(4, Integer.parseInt(phonenumberTextfield.getText()));
                pstmt.setString(5, passwordTextfield.getText());
                pstmt.setString(6, emailTextfield.getText());
                pstmt.setDate(7, Date.valueOf(dobPicker.getValue()));

                pstmt.executeUpdate();
            } catch (SQLException z) {
                System.out.println(z.getMessage());
            }

        } else{
            System.out.println("not filled");
        }

    }


}