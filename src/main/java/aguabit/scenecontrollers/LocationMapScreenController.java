package aguabit.scenecontrollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LocationMapScreenController implements Initializable {
    @FXML
    private WebView locationMapWebView = new WebView();
    private WebEngine locationMapWebEngine = new WebEngine();
    public static String locationMapCoordinates = "";

    private String openMapsString = "";
    private String googleMapsString = "";
    public boolean mapsProviderSwitch = false;
    @FXML
    private MFXButton mapsProviderSwitchButton = new MFXButton();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openMapsString = "https://www.openstreetmap.org/search?query="+locationMapCoordinates+"#map=18/"+locationMapCoordinates.split("\\,")[0]+"/"+locationMapCoordinates.split("\\,")[1];
        googleMapsString = "https://www.google.com/maps/place/"+locationMapCoordinates;

        if(mapsProviderSwitch){
            mapsProviderSwitchButton.setText("Switch to Open Maps");
        } else {
            mapsProviderSwitchButton.setText("Switch to Google Maps");
        }

        locationMapWebEngine = locationMapWebView.getEngine();
        locationMapWebEngine.load(openMapsString);
    }

    public void openInBrowser() throws IOException {
        if(mapsProviderSwitch){
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(googleMapsString));
        } else {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(openMapsString));
        }
    }
    public void switchMapProvider(){
        mapsProviderSwitch = !mapsProviderSwitch;
        if(mapsProviderSwitch){
            locationMapWebEngine.load(googleMapsString);
        } else {
            locationMapWebEngine.load(openMapsString);
        }
    }
}
