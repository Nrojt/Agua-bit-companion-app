package com.example.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;



public class LoginScreenController {
    @FXML
    TextField userNameTextField;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onLoginButtonCLick(ActionEvent event) throws IOException {
        String username = userNameTextField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        root = loader.load();

        MainScreenController test = (MainScreenController) loader.getController();
        test.displayName(username);

        //root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("MainScreen.css").toExternalForm());
        String css = this.getClass().getResource("MainScreen.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}