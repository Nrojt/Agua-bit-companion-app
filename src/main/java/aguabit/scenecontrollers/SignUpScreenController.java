package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUpScreenController implements Initializable{
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
    @FXML
    public Label informationLabel = new Label();

    int phoneNumber;

    protected String querry = "INSERT INTO user(first_name, last_name, username, phonenumber, password, email, date_of_birth) VALUES(?,?,?,?,?,?,?)";;


    public void printInput(ActionEvent e){
        if(!usernameTextfield.getText().isBlank() && !passwordTextfield.getText().isBlank() && !emailTextfield.getText().isBlank()) {
            String firstName = firstnameTextfield.getText();
            String lastName = lastnameTextfield.getText();
            String userName = usernameTextfield.getText();
            String password = passwordTextfield.getText();
            String email = emailTextfield.getText();
            String dob = String.valueOf(dobPicker.getValue());

            if(phonenumberTextfield.getText().matches("[0-9]+")) {
                phoneNumber = Integer.parseInt(phonenumberTextfield.getText());
            } else if (!phonenumberTextfield.getText().isBlank()) {
                informationLabel.setText("Not a valid phone number, please only enter numbers.");
            }

            System.out.println("username " + userName);
            System.out.println("firstname " + firstName);
            System.out.println("lastname " + lastName);
            System.out.println("phone number " + phonenumberTextfield.getText());
            System.out.println("email " + email);
            System.out.println("password " + password);
            System.out.println("date of birth " + dob);

            if((phonenumberTextfield.getText().matches("[0-9]+") && !phonenumberTextfield.getText().isBlank()) || phonenumberTextfield.getText().isBlank()) {
                DatabaseConnection connectionNow = new DatabaseConnection();
                try (Connection connectDB = connectionNow.getDBConnection();
                     PreparedStatement pstmt = connectDB.prepareStatement(querry)) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, userName);
                    pstmt.setInt(4, phoneNumber);
                    pstmt.setString(5, password);
                    pstmt.setString(6, email);
                    pstmt.setString(7, dob);
                    pstmt.executeUpdate();
                } catch (SQLException z) {
                    System.out.println(z.getMessage());
                }
                informationLabel.setText("Registration succesfull");
            }
        } else{
            informationLabel.setText("Not all required textfields filled in");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        informationLabel.setText("Information can be changed at a later date");
    }
}