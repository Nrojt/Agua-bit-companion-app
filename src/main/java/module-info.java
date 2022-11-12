module com.example.scenecontrollers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires MaterialFX;
    opens aguabit.scenecontrollers to javafx.fxml;
    exports aguabit.scenecontrollers;
}