package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsScreenController implements Initializable {
    @FXML
    private MFXRadioButton settingsTopMenuBarButton = new MFXRadioButton();
    @FXML
    private MFXRadioButton settingsSideMenuBarButton = new MFXRadioButton();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        if(MenuOverlayController.menuBarSide) {settingsSideMenuBarButton.setSelected(true);}
        else{settingsTopMenuBarButton.setSelected(true);}
    }
    public void menuBarToggle (ActionEvent event) throws IOException {
        if(settingsSideMenuBarButton.isSelected()){
            MenuOverlayController.menuBarSide = true;
        } else if (settingsTopMenuBarButton.isSelected()){
            MenuOverlayController.menuBarSide = false;
        }
    }

}
