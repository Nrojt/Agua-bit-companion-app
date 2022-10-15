package com.example.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ConnectScreenController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane scenePane;
    @FXML
    public ChoiceBox<String> slot1Sensor = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot2Sensor = new ChoiceBox();
    @FXML
    public ChoiceBox<String> slot3Sensor = new ChoiceBox();
    @FXML
    private Label informationLabel = new Label();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slot1Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot2Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot3Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
    }
    public void sendToProduct(ActionEvent action) {
        System.out.println(slot1Sensor.getValue()+"\n"+slot2Sensor.getValue()+"\n"+slot3Sensor.getValue());
        if (slot1Sensor.getValue().isEmpty() || slot2Sensor.getValue().isEmpty() || slot3Sensor.getValue().isEmpty()){
            informationLabel.setText("Please select a sensor");
        }
        else if (slot1Sensor.getValue().equals(slot2Sensor.getValue()) || slot1Sensor.getValue().equals(slot3Sensor.getValue()) || slot2Sensor.getValue().equals(slot3Sensor.getValue())) {
            //&& !slot1Sensor.getValue().equals("EMPTY") && !slot2Sensor.getValue().equals("EMPTY")
            informationLabel.setText("Select different");
        } else {
            informationLabel.setText("Information sent to product");
        }
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
    public void loginScreen(ActionEvent event) throws IOException{
        screenSwitcher("LoginScreen.fxml", "LoginScreen.css");
        stage.setResizable(false);
    }

    public void connectScreen(ActionEvent event) throws IOException{
        screenSwitcher("ConnectScreen.fxml", "ConnectScreen.css");
    }

    public void mainScreen() throws IOException{
        screenSwitcher("MainScreen.fxml", "MainScreen.css");
    }

    public void logout(ActionEvent event) throws IOException{
        //displayName("user");
    }

    public void accountScreen(ActionEvent event) throws IOException{
        screenSwitcher("AccountScreen.fxml", "AccountScreen.css");
    }

    public void measureScreen(ActionEvent event) throws IOException{
        screenSwitcher("MeasureScreen.fxml", "MeasureScreen.css");
    }
    public void updateScreen(ActionEvent event) throws IOException{
        screenSwitcher("UpdateScreen.fxml", "UpdateScreen.css");
    }
    public void setupScreen(ActionEvent event) throws IOException{
        screenSwitcher("SetupScreen.fxml", "SetupScreen.css");
    }

    public void aboutScreen(ActionEvent event) throws IOException{
        screenSwitcher("AboutScreen.fxml", "AboutScreen.css");
    }

    public void measureInfoScreen(ActionEvent e) throws IOException{
        screenSwitcher("MeasureInfoScreen.fxml", "MeasureInfoScreen.css");
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
}