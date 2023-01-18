package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import aguabit.savefile.SaveFile;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static aguabit.savefile.SaveFile.*;

public class SettingsScreenController implements Initializable {
    @FXML
    private MFXRadioButton settingsTopMenuBarButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton settingsSideMenuBarButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton settingsLightThemeButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton settingsDarkThemeButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton profilePicture1RadioButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton profilePicture2RadioButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton profilePicture3RadioButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton profilePicture4RadioButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton profilePictureCustomRadioButton = new MFXRadioButton();

    private final FileChooser customProfilePictureFileChooser = new FileChooser();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //code that makes the correct buttons selected on the settings screen.
        if (menuBarSide) {
            settingsSideMenuBarButton.setSelected(true);
        } else {
            settingsTopMenuBarButton.setSelected(true);
        }

        //code for having the correct profile picture selected on startup
        switch (profilePicture) {
            case -1 -> profilePictureCustomRadioButton.setSelected(true);
            case 2 -> profilePicture2RadioButton.setSelected(true);
            case 3 -> profilePicture3RadioButton.setSelected(true);
            case 4 -> profilePicture4RadioButton.setSelected(true);
            default -> profilePicture1RadioButton.setSelected(true);
        }

        //code for having the correct theme selected on startup
        switch (theme) {
            case 0 -> settingsLightThemeButton.setSelected(true);
            case 1 -> settingsDarkThemeButton.setSelected(true);
        }
    }

    //code for switching between the side and top menubar
    public void settingsButtonClicked(){
        if (settingsSideMenuBarButton.isSelected()) {
            menuBarSide = true;
        } else if (settingsTopMenuBarButton.isSelected()) {
            menuBarSide = false;
        }

        if(settingsDarkThemeButton.isSelected()){
            theme = 1;
        } else if (settingsLightThemeButton.isSelected()) {
            theme = 0;
        }

        if(profilePicture1RadioButton.isSelected()){
            profilePicture = 1;
        } else if(profilePicture2RadioButton.isSelected()){
            profilePicture = 2;
        } else if(profilePicture3RadioButton.isSelected()){
            profilePicture = 3;
        } else if (profilePicture4RadioButton.isSelected()) {
            profilePicture = 4;
        } else if (profilePictureCustomRadioButton.isSelected()){
            profilePicture = -1;
        }
        MenuOverlayController.profilePictureChanged = true;

        SaveFile.saveSettings();
    }

    public void customProfilePicturePicker(){
        FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
        customProfilePictureFileChooser.getExtensionFilters().add(exFilter);
        File selectedFile = customProfilePictureFileChooser.showOpenDialog(null);

        if(selectedFile != null){
            profilePicture = -1;
            SaveFile.customProfilePicturePath = selectedFile.getPath();
            MenuOverlayController.profilePictureChanged = true;
            settingsButtonClicked();
        }

    }
}