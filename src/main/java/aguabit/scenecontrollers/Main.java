package aguabit.scenecontrollers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import aguabit.savefile.SaveFile;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    static Scene scene;
    private String cssfile;

    //override gets run after loading in
    @Override
    public void start(Stage stage) throws IOException {
        //reading saved settings
        SaveFile.readSettingsFromFile();

        //code for setting either the dark or light theme.
        if(SaveFile.theme == 1){
            cssfile = "darkmode.css";
        }else if (SaveFile.theme == 0){
            cssfile = "lightmode.css";
        }

        //code for loading in the fxml and css file for running the application.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuOverlay.fxml"));
        scene = new Scene(fxmlLoader.load(),1280,720);
        stage.setTitle("Agua:bit companion app");
        String css = Objects.requireNonNull(this.getClass().getResource(cssfile)).toExternalForm();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

        //When someone presses the close button, custom code will run.
        stage.setOnCloseRequest(event -> exit(stage));
    }

    //this code gets executed when the user pressed the close button in the top right corner.
    public void exit (Stage stage){
        try {
            MenuOverlayController.driveDetector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //closing all the possible threads that could be open.
        try {
            MeasureScreenController.updateThreadRunning.set(false);
            MenuOverlayController.menuOverlayUpdateThreadRunning.set(false);
        }catch(NullPointerException ignored){}

        //what actually closes the program
        stage.close();
        Platform.exit();
    }

    //this actually runs the program
    public static void main(String[] args) {
        launch();
    }
}