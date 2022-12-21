package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import saveFile.SaveFile;

import java.io.IOException;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //code that makes the correct buttons selected on the settings screen.
        if (menuBarSide) {
            settingsSideMenuBarButton.setSelected(true);
        } else {
            settingsTopMenuBarButton.setSelected(true);
        }

        if(theme == 1){
            settingsDarkThemeButton.setSelected(true);
        } else if (theme == 0) {
            settingsLightThemeButton.setSelected(true);
        }
    }

    //code for switching between the side and top menubar
    public void settingsButtonClicked(ActionEvent event) throws IOException {
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

        saveSettings();
    }
}