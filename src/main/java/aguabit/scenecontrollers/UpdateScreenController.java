package aguabit.scenecontrollers;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.commons.io.*;

import javax.swing.*;

public class UpdateScreenController implements Initializable {
    public static Thread downloadingFirmware;
    public static Thread uploadingFirmware;
    private final String pathToDocumentFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private final String pathToFirmware = pathToDocumentFolder + "/AguaBit/Aguabit-firmware.hex";
    private char microBitDriveLetter = ' ';
    @FXML
    private Label notificationLabel = new Label();

    public void copyToMicroBit(ActionEvent e) throws IOException {
        if (!new File(pathToFirmware).isFile()) {
            notificationLabel.setText("Downloading firmware, please wait");
            downloadingFirmware = new Thread(this::downloadFiles);
            downloadingFirmware.start();
        } else if(MenuOverlayController.isAguabitConnected && microBitDriveLetter != ' '){
            notificationLabel.setText("Uploading firmware to Agua:bit, please wait");
            uploadingFirmware = new Thread(this::setUploadingFirmware);
            uploadingFirmware.start();
        }
        //notificationLabel.setText("Click the button to update the firmware");
    }

    private void downloadFiles(){
        try {
            FileUtils.copyURLToFile(new URL("https://github.com/Nrojt/AguaBit-firmware/releases/download/v1.0.0/Aguabit-firmware.hex"), new File(pathToFirmware));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {notificationLabel.setText("Download done, click the button to upload the firmware");});
    }

    private void setUploadingFirmware(){
        Path firmwareLocation = Paths.get(pathToFirmware);
        Path microbitDrive = Paths.get(microBitDriveLetter+ ":\\Aguabit-firmware.hex");
        System.out.println(microbitDrive);
        try {
            Files.copy(firmwareLocation, microbitDrive, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {notificationLabel.setText("Upload done, the Agua:bit is ready for use");});
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MenuOverlayController.isAguabitConnected) {
            microBitDriveLetter = MenuOverlayController.driveDetector.getRemovableDevices().toString().charAt(32);
            notificationLabel.setText("Click the button to update the firmware");
        }
        else{notificationLabel.setText("Please connect the Agua:bit and refresh this page");}
    }
}
