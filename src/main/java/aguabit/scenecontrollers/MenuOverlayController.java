package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import saveFile.SaveFile;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import net.samuelcampos.usbdrivedetector.*;


public class MenuOverlayController implements Initializable {
    //variables for the windows
    private static Stage stage;
    @FXML
    private BorderPane menuPane;
    @FXML
    public AnchorPane fxmlPane;
    public static Thread menuUpdateThread;

    //variables for text in the menubar
    public static String userName = "User";
    public static boolean loginStatus = false;
    @FXML
    public Label userNameLabel;

    //variables for the menu overlay
    public static boolean menuBarSide = SaveFile.menuBarSide;
    @FXML
    private Button menuToggle = new Button();
    @FXML
    private AnchorPane sideMenuBar = new AnchorPane();
    @FXML
    private MFXButton loginButton = new MFXButton();
    @FXML
    private MenuBar topMenuBar = new MenuBar();
    @FXML
    private ImageView menuToggleThreeLines = new ImageView();
    @FXML
    private Label AguabitConnectedStatus = new Label();
    public static boolean isAguabitConnected = false;

    public static char driveLetter;

    public static USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();

    public static int userId = 1;

    public MenuOverlayController() throws IOException {
        //this runs every time this controller gets loaded, which should only be once at startup.
    }


    //Override runs after the scene is loaded. This can be used to change text.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //driveDetector.addDriveListener(System.out::println); //for printing out when usb devices connect or disconnect
        //loading in the mainscreen fxml file
        try {
            screenSwitcher("MainScreen.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //stolen from https://www.youtube.com/watch?v=Celi0vRJtoo
        //making a new thread to run the menu updates on
        menuUpdateThread = new Thread(this::menuUpdate);
        menuUpdateThread.start();
    }

    //the code for loading in the different windows
    public void screenSwitcher(String fxmlFile) throws IOException{
        MeasureScreenController.shouldMeasureScreenUpdate = false;
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        fxmlPane.getChildren().setAll(pane);
    }

    //the login screen gets opened in a new window, so it cannot use the screenSwitcher code.
    public void loginScreen(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(root1);
        stage2.setTitle("Agua:bit account login");
        stage2.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        String css = Objects.requireNonNull(this.getClass().getResource("LoginScreen.css")).toExternalForm();
        scene2.getStylesheets().add(css);
        stage2.setScene(scene2);
        stage2.setResizable(false);
        stage2.show();
    }

    //code for all the clickable buttons
    public void connectScreen(ActionEvent event) throws IOException{
        screenSwitcher("ConnectScreen.fxml");
    }
    public void mainScreen(ActionEvent event) throws IOException{
        screenSwitcher("MainScreen.fxml");
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
    public void settingsScreen(ActionEvent e) throws IOException{
        screenSwitcher("SettingsScreen.fxml");
    }
    public void databaseScreen(ActionEvent e) throws IOException{
        screenSwitcher("DatabaseScreen.fxml");
    }

    //code for the logout button
    public void logout(ActionEvent event){
        loginStatus = false;
        userName = "user";
        if (menuPane.getChildren().contains(userNameLabel)){
            userNameLabel.setText(userName);
        }
        loginButton.setText("Login");
    }

    //code for the exit button
    public void exit (ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to close the application");
        alert.setContentText("Do you want to exit?");

        if(alert.showAndWait().get()== ButtonType.OK) {
            MeasureScreenController.measureThread.stop();
            UpdateScreenController.uploadingFirmware.stop();
            UpdateScreenController.downloadingFirmware.stop();
            driveDetector.close();
            stage = (Stage) menuPane.getScene().getWindow();
            stage.close();
            Platform.exit();
        }
    }

    //code for the login/logout button on the side menu bar
    public void loginStatusToggle(ActionEvent event) throws IOException{
        if (!loginStatus){
            loginScreen(event);
        }
        else{logout(event);}
    }
    //code for the button to toggle the visibility of the sidemenubar
    public void sideMenuToggle(ActionEvent event){
        sideMenuBar.setVisible(!sideMenuBar.isVisible());
    }


    //updating the menu overlay, this is run on a separate thread
    public void menuUpdate() {
        while (true) {

            //
            String[] connectedDrivesString = new String[0];
            Object[] connectedDrivesObject = driveDetector.getRemovableDevices().toArray();
            try{
               connectedDrivesString = new String[connectedDrivesObject.length];
               for(int i = 0; i < connectedDrivesObject.length; i++){
                   connectedDrivesString[i] = driveDetector.getRemovableDevices().get(i).toString();
               }

            } catch (IndexOutOfBoundsException e) {}

            for (String s : connectedDrivesString) {
                try {
                    if (s.contains("MICROBIT")) {
                        isAguabitConnected = true;
                        driveLetter = s.charAt(31);
                    }
                    else{
                        isAguabitConnected = false;
                    }
                } catch (NullPointerException ignored) {
                }
            }

            //platform.runlater makes the code in it run on the same thread as the menu, not on the newly made thread
            Platform.runLater(() -> {
                menuBarSide = SaveFile.menuBarSide;
                if (menuBarSide) {
                    topMenuBar.setVisible(false);
                    topMenuBar.setDisable(true);
                    sideMenuBar.setDisable(false);
                    menuToggle.setVisible(true);
                    menuToggleThreeLines.setVisible(true);
                    menuToggle.setDisable(false);
                    menuToggleThreeLines.setDisable(false);
                } else {
                    topMenuBar.setVisible(true);
                    topMenuBar.setDisable(false);
                    sideMenuBar.setDisable(true);
                    sideMenuBar.setVisible(false);
                    menuToggle.setVisible(false);
                    menuToggleThreeLines.setVisible(false);
                    menuToggle.setDisable(true);
                    menuToggleThreeLines.setDisable(true);
                }
                if (userName.isEmpty()) {
                    userName = "User";
                }
                userNameLabel.setText(userName);


                if (loginStatus) {
                    loginButton.setText("Logout");
                } else {
                    loginButton.setText("Login");
                }

                if(isAguabitConnected){
                    AguabitConnectedStatus.setText("Agua:bit connected");
                }
                else {AguabitConnectedStatus.setText("Agua:bit not connected");}
            });
            //pausing the thread
            try{Thread.sleep(100);}
            catch (InterruptedException ignored){}
        }
    }
}
