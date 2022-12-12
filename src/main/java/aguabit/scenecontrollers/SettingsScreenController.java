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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (menuBarSide) {
            settingsSideMenuBarButton.setSelected(true);
        } else {
            settingsTopMenuBarButton.setSelected(true);
        }
    }
    //code for switching between the side and top menubar
    public void menuBarToggle(ActionEvent event) throws IOException {
        if (settingsSideMenuBarButton.isSelected()) {
            menuBarSide = true;
        } else if (settingsTopMenuBarButton.isSelected()) {
            menuBarSide = false;
        }
        saveSettings();
    }
    // public static boolean islightmode = true;
    //public void Changemode (ActionEvent event){
    //  islightmode =!islightmode;
    //if(islightmode){
    // setlightmode();}
    //  else {
    //   setDarkMode();}
    //}
    // (-pratik)
}