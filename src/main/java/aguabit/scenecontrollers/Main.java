package aguabit.scenecontrollers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.image.Image;

public class Main extends Application {
    //override gets run after loading in
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuOverlay.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1280,720);
        stage.setTitle("Agua:bit companion app");
        String css = this.getClass().getResource("MainScreen.css").toExternalForm();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> exit(stage));
    }

    public void exit (Stage stage){
        MenuOverlayController.menuUpdateThread.stop();
        stage.close();
    }

    //this actually runs the program
    public static void main(String[] args) {
        launch();
    }
}