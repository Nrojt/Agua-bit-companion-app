package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import saveFile.SaveFile;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private WebView mainPageWebView = new WebView();
    private WebEngine mainPageWebEngine = new WebEngine();

    public MainScreenController() {
        //okay so this gets run every time a screen loads
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameLabel.setText("Hello "+ MenuOverlayController.userName);
        if(SaveFile.theme == 1) {
            mainPageWebView.setPageFill(Color.rgb(100,100,100, 0.13));
        } else if (SaveFile.theme == 0){
            mainPageWebView.setPageFill(Color.rgb(255, 255, 255, 0.75));
        }
        mainPageWebEngine = mainPageWebView.getEngine();
        mainPageWebEngine.load("https://github.com/Nrojt/Agua-bit-companion-app/");
    }
}
