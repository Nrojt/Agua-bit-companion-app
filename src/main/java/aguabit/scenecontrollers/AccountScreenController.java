package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AccountScreenController implements Initializable {
    //labels on top of the screen
    @FXML
    private Label userNameLabel;
    @FXML
    private Label emailLabel;

    //textfields so the user can fill in new information
    @FXML
    private TextField oldPasswordTextfield = new TextField();
    @FXML
    private  TextField newPasswordTextfield = new TextField();
    @FXML
    private TextField passwordTextfield = new TextField();
    @FXML
    private TextField oldEmailTextfield = new TextField();
    @FXML
    private TextField newEmailTextfield = new TextField();
    @FXML
    private TextField firstNameTextfield = new TextField();
    @FXML
    private TextField lastNameTextfield = new TextField();
    @FXML
    private TextField userNameTextfield = new TextField();
    @FXML
    private TextField phoneNumberTextfield = new TextField();
    @FXML
    private TextField EmailForPasswordTextfield = new TextField();

    private String username = MenuOverlayController.userName;
    private String email;
    private String firstName;
    private String lastName;
    private  String password;
    private String phoneNumber;
    private int profilePicture;

    //TODO make the labels update with the user information
    //TODO adding the ability to change user information in the database

    //setting all the labels
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(MenuOverlayController.loginStatus) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getDBConnection();
            String accountInfoQuery = "SELECT username,email, first_name, last_name, phonenumber, profilepicture, password FROM user WHERE user_id = '"+ MenuOverlayController.userId +"'";
            Statement accountInfoStatement;
            ResultSet result;

            try {
                accountInfoStatement = connectDB.createStatement();
                result = accountInfoStatement.executeQuery(accountInfoQuery);
                username = result.getString(1);
                email = result.getString(2);
                firstName = result.getString(3);
                lastName = result.getString(4);
                phoneNumber = result.getString(5);
                profilePicture = result.getInt(6);
                password = result.getString(7);
                connectDB.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            userNameLabel.setText(username);
            emailLabel.setText(email);

            firstNameTextfield.setText(firstName);
            lastNameTextfield.setText(lastName);
            userNameTextfield.setText(username);
            phoneNumberTextfield.setText(phoneNumber);
            newEmailTextfield.setText(email);

        }
    }

    //code for changing the password in the database
    public void updatePassword(){
        if(MenuOverlayController.loginStatus) {
            if (oldPasswordTextfield.getText().equals(password) && !newPasswordTextfield.getText().isBlank() && EmailForPasswordTextfield.getText().equals(email)) {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getDBConnection();
                String updatePasswordQuery = "UPDATE user SET password = '" + newPasswordTextfield.getText() + "' WHERE user_id = '" + MenuOverlayController.userId + "'";

                try {
                    PreparedStatement pstmt = connectDB.prepareStatement(updatePasswordQuery);
                    pstmt.executeUpdate();
                    connectDB.close();
                } catch (SQLException z) {
                    System.out.println(z.getMessage());
                }
            } else {
                System.out.println("Filled in information for changing password is not correct");
            }
        } else{
            System.out.println("User not logged in");
        }
    }

    //code for updating the other user information in the database
    public void updateInformation(){
        if(MenuOverlayController.loginStatus) {
            if (oldEmailTextfield.getText().equals(email) && passwordTextfield.getText().equals(password) && !newEmailTextfield.getText().isBlank() && Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(newEmailTextfield.getText()).matches()) {
                String phoneNumberCheck = null;
                if (!phoneNumberTextfield.getText().isBlank() && phoneNumberTextfield.getText().matches("[0-9]+")) {
                    phoneNumberCheck = phoneNumberTextfield.getText();
                } else {
                    phoneNumberTextfield.setText(phoneNumber);
                    System.out.println("Not a valid phone number, please only enter numbers.");
                }
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getDBConnection();
                String updateInformationQuery = "UPDATE user SET first_name = '"+ firstNameTextfield.getText() + "', last_name = '"+lastNameTextfield.getText()+"', username = '"+userNameTextfield.getText()+"', phonenumber = '"+phoneNumberCheck+"', email = '"+newEmailTextfield.getText()+"' WHERE user_id = '"+MenuOverlayController.userId+"'";
                try {
                    PreparedStatement pstmt = connectDB.prepareStatement(updateInformationQuery);
                    pstmt.executeUpdate();
                    connectDB.close();
                } catch (SQLException z) {
                    System.out.println(z.getMessage());
                }
            }
        } else {
            System.out.println("USer not logged in");
        }
    }
}
