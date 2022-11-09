package com.example.scenecontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;

public class MainController implements Initializable {
    static String userName = "user";
    static boolean loginStatus = false;
    static boolean menuBarSide = true;


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label userNameLabel;
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
    @FXML
    private Button menuToggle = new Button();
    @FXML
    private AnchorPane sideMenuBar = new AnchorPane();
    @FXML
    private MFXButton loginButton = new MFXButton();
    @FXML
    private MenuBar topMenuBar = new MenuBar();
    @FXML
    private MFXRadioButton settingsTopMenuBarButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton settingsSideMenuBarButton = new MFXRadioButton();
    @FXML
    private ImageView menuToggleThreeLines = new ImageView();

    public MainController() {
        System.out.println();
        //okay so this gets run every time a screen loads
    }

    // Switching the screens
    public void screenSwitcher(String fxmlFile) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage)scenePane.getScene().getWindow();
        String css = this.getClass().getResource("MainScreen.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    //Main screen


    //account screen

    //about screen

    //connect screen
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(menuBarSide){
            topMenuBar.setVisible(false);
            topMenuBar.setDisable(true);
            sideMenuBar.setDisable(false);
            menuToggle.setVisible(true);
            menuToggleThreeLines.setVisible(true);
            menuToggle.setDisable(false);
            menuToggleThreeLines.setDisable(false);
        }
        else{
            topMenuBar.setVisible(true);
            topMenuBar.setDisable(false);
            sideMenuBar.setDisable(true);
            menuToggle.setVisible(false);
            menuToggleThreeLines.setVisible(false);
            menuToggle.setDisable(true);
            menuToggleThreeLines.setDisable(true);
        }

        slot1Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot2Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        slot3Sensor.getItems().addAll("PH-Sensor", "Temperature sensor", "sensor 3", "EMPTY");
        if (scenePane.getChildren().contains(userNameLabel)) {
            if (userName.isEmpty()) {
                userName = "user";
            }
            userNameLabel.setText(userName);
        }
        if(loginStatus){
                loginButton.setText("Logout");
        }
        else if (!loginStatus){
                loginButton.setText("Login");
        }



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

    //measure info screen

    //measure screen

    //setup screen

    //update screen

    //settings screen


    //menubar
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

    public void sideMenuToggle(ActionEvent event){
        sideMenuBar.setVisible(!sideMenuBar.isVisible());
    }

    public void menuBarToggle (ActionEvent event){
        if(settingsSideMenuBarButton.isSelected()){
            menuBarSide = true;
        } else if (settingsTopMenuBarButton.isSelected()){
            menuBarSide = false;
        }
    }


    public void loginStatusToggle(ActionEvent event) throws IOException{
        if (loginStatus == false){
            loginScreen(event);
        }
        else{logout(event);}
    }

    public void loginScreen(ActionEvent event) throws IOException{
        screenSwitcher("LoginScreen.fxml");
        stage.setResizable(false);
        loginStatus = true;
    }

    public void connectScreen(ActionEvent event) throws IOException{
        screenSwitcher("ConnectScreen.fxml");
    }

    public void mainScreen() throws IOException{
        screenSwitcher("MainScreen.fxml");
    }

    public void logout(ActionEvent event) throws IOException{
        loginStatus = false;
        userName = "user";
        if (scenePane.getChildren().contains(userNameLabel)){
            userNameLabel.setText(userName);
        }
        loginButton.setText("Login");
    }

    public void accountScreen(ActionEvent event) throws IOException{
        screenSwitcher("AccountScreen.fxml");
    }

    public void measureScreen(ActionEvent event) throws IOException{
        screenSwitcher("MeasureScreen.fxml");
    }
    public void updateScreen(ActionEvent event) throws IOException{
        screenSwitcher("UpdateScreen.fxml");
    }
    public void setupScreen(ActionEvent event) throws IOException{
        screenSwitcher("SetupScreen.fxml");
    }

    public void aboutScreen(ActionEvent event) throws IOException{
        screenSwitcher("AboutScreen.fxml");
    }

    public void measureInfoScreen(ActionEvent e) throws IOException{
        screenSwitcher("MeasureInfoScreen.fxml");
    }

    public void settingsScreen(ActionEvent e) throws IOException{
        screenSwitcher("SettingsScreen.fxml");
    }
}
