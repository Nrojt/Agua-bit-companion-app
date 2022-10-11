module com.example.scenecontrollers {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.scenecontrollers to javafx.fxml;
    exports com.example.scenecontrollers;
}