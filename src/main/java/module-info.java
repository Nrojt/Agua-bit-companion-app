module com.example.scenecontrollers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires MaterialFX;
    requires com.fazecast.jSerialComm;
    requires usbdrivedetector;
    opens aguabit.scenecontrollers to javafx.fxml;
    exports aguabit.scenecontrollers;
}