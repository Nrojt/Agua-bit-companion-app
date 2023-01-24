package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import aguabit.savefile.SaveFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
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
    private TextField emailForPasswordTextfield = new TextField();
    @FXML
    private TextField passwordForDeleteTextfield = new TextField();
    @FXML
    private TextField emailForDeleteTextfield = new TextField();

    //variables for on screen items that need to be changed while running
    @FXML
    private AnchorPane accountPane = new AnchorPane();
    @FXML
    private Label informationLabel = new Label();
    @FXML
    private ImageView profilePictureAccount = new ImageView();

    // strings
    private String email;
    private String password;
    private String phoneNumber;


    //setting all the labels
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(MenuOverlayController.loginStatus) {
            //getting the user information from the database
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getDBConnection();
            String accountInfoQuery = "SELECT username,email, first_name, last_name, phonenumber, password FROM user WHERE user_id = '"+ MenuOverlayController.userId +"'";
            Statement accountInfoStatement;
            ResultSet result;

            String username;
            String firstName;
            String lastName;
            try {
                accountInfoStatement = connectDB.createStatement();
                result = accountInfoStatement.executeQuery(accountInfoQuery);
                username = result.getString(1);
                email = result.getString(2);
                firstName = result.getString(3);
                lastName = result.getString(4);
                phoneNumber = result.getString(5);
                password = result.getString(6);
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

        //pressing enter within these fields triggers updatePassword()
        oldPasswordTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updatePassword();
            }
        });
        newPasswordTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updatePassword();
            }
        });
        emailForPasswordTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updatePassword();
            }
        });

        //pressing enter within these fields triggers updateInformation()
        firstNameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        lastNameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        userNameTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        phoneNumberTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        newEmailTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        oldEmailTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });
        passwordTextfield.setOnKeyPressed(keypress -> {
            if (keypress.getCode() == KeyCode.ENTER) {
                updateInformation();
            }
        });


        MenuOverlayController.checkIfCustomFileExist = new File(SaveFile.customProfilePicturePath);

        switch (SaveFile.profilePicture) {
            case -1 -> {
                if (!SaveFile.customProfilePicturePath.isBlank() && MenuOverlayController.checkIfCustomFileExist.isFile()) {
                    profilePictureAccount.setImage(new Image((SaveFile.customProfilePicturePath)));
                } else {
                    profilePictureAccount.setImage(new Image(Objects.requireNonNull(MenuOverlayController.class.getResourceAsStream("images/profilepictures/profilePictureDefault.png"))));
                }
            }
            default ->
                    profilePictureAccount.setImage(new Image(Objects.requireNonNull(MenuOverlayController.class.getResourceAsStream("images/profilepictures/profilePictureDefault.png"))));
            case 2 ->
                    profilePictureAccount.setImage(new Image(Objects.requireNonNull(MenuOverlayController.class.getResourceAsStream("images/profilepictures/profilePictureBlue.png"))));
            case 3 ->
                    profilePictureAccount.setImage(new Image(Objects.requireNonNull(MenuOverlayController.class.getResourceAsStream("images/profilepictures/profilePictureRed.png"))));
            case 4 ->
                    profilePictureAccount.setImage(new Image(Objects.requireNonNull(MenuOverlayController.class.getResourceAsStream("images/profilepictures/profilePictureTorquise.png"))));
        }
    }

    //code for changing the password in the database
    public void updatePassword(){
        if(MenuOverlayController.loginStatus) {
            if (oldPasswordTextfield.getText().equals(password) && !newPasswordTextfield.getText().isBlank() && emailForPasswordTextfield.getText().equals(email)) {
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
                informationLabel.setText("Account information incorrect");
            }
        } else{
            informationLabel.setText("User not logged in");
        }
    }

    //code for updating the other user information in the database
    public void updateInformation(){
        if(MenuOverlayController.loginStatus) {
            if (oldEmailTextfield.getText().equals(email) && passwordTextfield.getText().equals(password) && !newEmailTextfield.getText().isBlank()) {
                String phoneNumberCheck = null;
                String emailCheck = null;
                if(Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(newEmailTextfield.getText()).matches()){
                    emailCheck = newEmailTextfield.getText();
                } else {
                    informationLabel.setText("Not a valid e-mail adress");
                }
                if (!phoneNumberTextfield.getText().isBlank() && phoneNumberTextfield.getText().matches("[0-9]+")) {
                    phoneNumberCheck = phoneNumberTextfield.getText();
                } else {
                    phoneNumberTextfield.setText(phoneNumber);
                    informationLabel.setText("Not a valid phone number, please only enter numbers.");
                }
                if((phoneNumberTextfield.getText().isBlank() || phoneNumberCheck != null) && emailCheck != null) {
                    DatabaseConnection connection = new DatabaseConnection();
                    Connection connectDB = connection.getDBConnection();
                    String updateInformationQuery = "UPDATE user SET first_name = '" + firstNameTextfield.getText() + "', last_name = '" + lastNameTextfield.getText() + "', username = '" + userNameTextfield.getText() + "', phonenumber = '" + phoneNumberCheck + "', email = '" + emailCheck + "' WHERE user_id = '" + MenuOverlayController.userId + "'";
                    try {
                        PreparedStatement pstmt = connectDB.prepareStatement(updateInformationQuery);
                        pstmt.executeUpdate();
                        connectDB.close();
                    } catch (SQLException z) {
                        System.out.println(z.getMessage());
                    }
                }
            }
        } else {
            informationLabel.setText("User not logged in");
        }
    }

    //for deleting an account
    public void deleteAccount(){
        if (passwordForDeleteTextfield.getText().equals(password) && emailForDeleteTextfield.getText().equals(email)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete account");
            alert.setHeaderText("You're about to delete your account");
            alert.setContentText("Are you sure you want to delete this account?");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));

            //Showing a prompt when the menu exit button is clicked, to make sure the user wants to quit
            if(alert.showAndWait().get()== ButtonType.OK) {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getDBConnection();
                String deletePasswordQuery = "DELETE FROM user WHERE user_id = '" + MenuOverlayController.userId + "'";

                try {
                    PreparedStatement pstmt = connectDB.prepareStatement(deletePasswordQuery);
                    pstmt.executeUpdate();
                    connectDB.close();
                    MenuOverlayController.loginStatus = false;
                    MenuOverlayController.userId = -1;
                    MenuOverlayController.userName = "User";
                    AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AccountScreen.fxml")));
                    accountPane.getChildren().setAll(pane);
                } catch (SQLException z) {
                    System.out.println(z.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            informationLabel.setText("Account information incorrect");
        }
    }
}
