package com.example.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginScreenController {
    @FXML
    TextField userNameTextField;

    @FXML
    Hyperlink signupURL = new Hyperlink();
    @FXML
    Hyperlink forgotURL = new Hyperlink();
    private Stage stage;
    private Scene scene;
    private Parent root;



    public void onLoginButtonCLick(ActionEvent event) throws IOException {
        String username = userNameTextField.getText();
        MainController.userName = username;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("MainScreen.css").toExternalForm();
        stage.setResizable(true);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void onForgetURL (ActionEvent even){
    }
}