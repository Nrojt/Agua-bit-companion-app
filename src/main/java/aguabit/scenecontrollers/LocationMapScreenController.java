package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class LocationMapScreenController implements Initializable {
    @FXML
    private WebView locationMapWebView = new WebView();
    private WebEngine locationMapWebEngine = new WebEngine();
    public static String locationMapCoordinates = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationMapWebEngine = locationMapWebView.getEngine();
        locationMapWebEngine.load("https://www.google.com/maps/place/"+locationMapCoordinates);
    }
}
