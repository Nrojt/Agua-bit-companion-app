package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import saveFile.SaveFile;

import java.net.URL;
import java.util.ResourceBundle;

import static saveFile.SaveFile.*;

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

    //TODO make this screen look better
    //TODO add the option to pick a profile picture

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //code that makes the correct buttons selected on the settings screen.
        if (menuBarSide) {
            settingsSideMenuBarButton.setSelected(true);
        } else {
            settingsTopMenuBarButton.setSelected(true);
        }

        //code for having the correct profile picture selected on startup
        switch(profilePicture){
            case 2:
                profilePicture2RadioButton.setSelected(true);
                break;
            case 3:
                profilePicture3RadioButton.setSelected(true);
                break;
            case 4:
                profilePicture4RadioButton.setSelected(true);
                break;
            case 1:
            default:
                profilePicture1RadioButton.setSelected(true);
                break;
        }

        //code for having the correct theme selected on startup
        switch(theme) {
            case 0:
                settingsLightThemeButton.setSelected(true);
                break;
            case 1:
                settingsDarkThemeButton.setSelected(true);
                break;
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
        }

        SaveFile.saveSettings();
    }
}