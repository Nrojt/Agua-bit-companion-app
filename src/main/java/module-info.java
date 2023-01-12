module com.example.scenecontrollers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires MaterialFX;
    requires com.fazecast.jSerialComm;
    requires usbdrivedetector;
    requires org.apache.commons.io;
    requires java.sql;
    requires com.maxmind.geoip2;
    requires javafx.web;
    opens aguabit.scenecontrollers to javafx.fxml;
    exports aguabit.scenecontrollers;
    exports aguabit.location;
    opens aguabit.location to javafx.fxml;
}