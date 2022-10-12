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
    private MenuItem connectButton;

    public MainScreenController() {
    }

    public void screenSwitcher(String fxmlFile, String cssFile) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)scenePane.getScene().getWindow();
        String css = this.getClass().getResource(cssFile).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

        if(fxmlFile.equals("MainScreen.fxml")){
            MainScreenController main1 = (MainScreenController) fxmlLoader.getController();
            main1.displayName("user");
        }
    }

    public void displayName(String username){
        if(username.isEmpty()){
            username = "user";
        }
        if(username.equals("")){ username = "user";}
        userNameLabel.setText("Hello "+ username);
    }

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

    public void loginScreen(ActionEvent event) throws IOException{
        screenSwitcher("Login.fxml", "LoginScreen.css");
    }

    public void connectScreen(ActionEvent event) throws IOException{
        screenSwitcher("ConnectScreen.fxml", "ConnectScreen.css");
    }

    public void mainScreen() throws IOException{
        screenSwitcher("MainScreen.fxml", "MainScreen.css");
    }

    public void logout(ActionEvent event) throws IOException{
        displayName("user");
    }

}
