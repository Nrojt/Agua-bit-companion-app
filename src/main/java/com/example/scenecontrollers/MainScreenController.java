package com.example.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainScreenController {
    @FXML
    Label userNameLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane scenePane;
    @FXML
    private MenuItem exitButton;
    @FXML
    private MenuItem loginButton;
    @FXML
    private Button loginButton2;
    @FXML
    ImageView phImageView;

    public void exit (ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to close the application");
        alert.setContentText("Do you want to exit?");

        if(alert.showAndWait().get()== ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void displayName(String username){
        if(username.isEmpty()){
            username = "user";
        }
        userNameLabel.setText("Hello "+ username);
    }

    @FXML
    public void loginScreen(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)scenePane.getScene().getWindow();
        String css = this.getClass().getResource("LoginScreen.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }

}
