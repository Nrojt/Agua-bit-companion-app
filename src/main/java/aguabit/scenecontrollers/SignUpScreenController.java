package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    @FXML
    public AnchorPane fxmlPane;

    long phoneNumber;

    protected String querry = "INSERT INTO user(first_name, last_name, username, phonenumber, password, email, date_of_birth) VALUES(?,?,?,?,?,?,?)";


    public void signUp() {
        if (!usernameTextfield.getText().isBlank() && !passwordTextfield.getText().isBlank() && !emailTextfield.getText().isBlank()) {
            String firstName = firstnameTextfield.getText();
            String lastName = lastnameTextfield.getText();
            String userName = usernameTextfield.getText();
            String password = passwordTextfield.getText();
            String email = emailTextfield.getText();
            String dob = String.valueOf(dobPicker.getValue());

            if (phonenumberTextfield.getText().matches("[0-9]+")) {
                phoneNumber = Long.parseLong(phonenumberTextfield.getText());
            } else if (!phonenumberTextfield.getText().isBlank()) {
                informationLabel.setText("Not a valid phone number, please only enter numbers.");
            }


            if ((phonenumberTextfield.getText().matches("[0-9]+")) || phonenumberTextfield.getText().isBlank()) { //checking if input is a number, via regex
                if (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).matches()) { //checking if the input follows how an email should look, via regex
                    try {
                        if (checkEmail(email)) {
                            try {
                                DatabaseConnection connectionNow = new DatabaseConnection();
                                Connection connectDB = connectionNow.getDBConnection();
                                PreparedStatement pstmt = connectDB.prepareStatement(querry);
                                pstmt.setString(1, firstName);
                                pstmt.setString(2, lastName);
                                pstmt.setString(3, userName);
                                pstmt.setLong(4, phoneNumber);
                                pstmt.setString(5, password);
                                pstmt.setString(6, email);
                                pstmt.setString(7, dob);
                                pstmt.executeUpdate();

                                connectDB.close();

                            } catch (SQLException z) {
                                System.out.println(z.getMessage());
                            }
                            informationLabel.setText("Registration succesfull");
                        } else {
                            informationLabel.setText("This email is already in use");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else{
                    informationLabel.setText("This email adres is not accepted, please check if it is correct or use another email");
                }
                } else if (!phonenumberTextfield.getText().isBlank()) {
                    informationLabel.setText("Not a valid phone number, please only enter numbers");
                }
        }else {
            informationLabel.setText("Not all required textfields filled in");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //pressing enter in these fields will trigger the signup code
        usernameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });
        emailTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });
        passwordTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });

        firstnameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });
        lastnameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });
        phonenumberTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                signUp();
            }
        });

    }

    public boolean checkEmail(String email) throws SQLException {
        boolean doesEmailExist = false;

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getDBConnection();
        String checkEmailQuerry = "SELECT email FROM user WHERE email = '"+ email +"'";

        Statement emailCheck = connectDB.createStatement();
        ResultSet result = emailCheck.executeQuery(checkEmailQuerry);
        if(result.next()){
            System.out.println("bestaat");
            doesEmailExist = true;
        }
        else{System.out.println("bestaat niet");}

        connectDB.close();
        emailCheck.close();
        result.close();

        return !doesEmailExist;
    }

    public void backToLoginScreen() throws IOException {
        screenSwitcher("LoginScreen.fxml");
    }


    public void screenSwitcher(String fxmlFile) throws IOException {
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        fxmlPane.getChildren().setAll(pane);
    }
}