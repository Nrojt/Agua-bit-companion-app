package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
        mainPageWebView.setContextMenuEnabled(false);
        mainPageWebEngine = mainPageWebView.getEngine();
        mainPageWebEngine.load("https://github.com/Nrojt/Agua-bit-companion-app/");
    }
}
