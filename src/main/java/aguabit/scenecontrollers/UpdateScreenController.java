package aguabit.scenecontrollers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.commons.io.FileUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class UpdateScreenController implements Initializable {
    private static boolean alreadyDownloading = false;
    private static boolean alreadyUploading = false;
    private static boolean waitingAfterUploadingBoolean = false;
    public static Thread downloadingFirmware;
    public static Thread uploadingFirmware;
    private static Thread waitingAfterUploading;

    //These Strings are placed here, so they can be easily updated if required
    private final String pathToDocumentFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private final String pathToFirmware = pathToDocumentFolder + "/AguaBit/firmware/Aguabit-firmware.hex";
    private final String urlToFirmware = "https://github.com/Nrojt/AguaBit-firmware/releases/download/v1.5.0/Aguabit-firmware.hex";
    @FXML
    private Label informationLabel = new Label();

    //code for starting the thread for copying the microbit hex file to the microbit
    public void copyToMicroBit(){
        //checks if the .hex file exists
        if (!new File(pathToFirmware).isFile()) {
                if(!alreadyDownloading) {
                    informationLabel.setText("Downloading firmware, please wait.");
                    downloadingFirmware = new Thread(this::downloadFiles);
                    downloadingFirmware.start();
                } else {
                    informationLabel.setText("Already downloading, please wait.");
                }
            }
        //if it doesn't exist, downloading the hex file from urlToFirmware
        else if(MenuOverlayController.isAguabitConnected){
            if(!alreadyUploading && !waitingAfterUploadingBoolean) {
                informationLabel.setText("Uploading firmware to Agua:bit, please wait.");
                waitingAfterUploading = new Thread(this::waitAfterUploading);
                uploadingFirmware = new Thread(this::uploadingFirmware);
                uploadingFirmware.start();
            } else if(!waitingAfterUploadingBoolean){
                informationLabel.setText("Already uploading the firmware, please wait");
            } else{
                informationLabel.setText("Please wait a bit before trying to upload to the Agua:bit again");
            }
        }
        else{
            informationLabel.setText("Please connect the Agua:bit and reopen this page to upload.");
        }
    }

    //button for (re)downloading the hex file
    public void downloadButton(){
        if(!alreadyDownloading) {
            informationLabel.setText("Downloading firmware, please wait.");
            downloadingFirmware = new Thread(this::downloadFiles);
            downloadingFirmware.start();
        } else {
            informationLabel.setText("Already downloading, please wait.");
        }
    }
    //actual code for downloading the hex file
    private void downloadFiles(){
        if(isReachable(urlToFirmware)) {
            alreadyDownloading = true;
            //deleting the file if it already exists
            if (new File(pathToFirmware).isFile()) {
                new File(pathToFirmware).delete();
            }
            try {
                FileUtils.copyURLToFile(new URL(urlToFirmware), new File(pathToFirmware), 5000, 5000);
            } catch (IOException | RuntimeException e) {
                Platform.runLater(() -> informationLabel.setText("An error has occurred, please try again later"));
            }
            if (MenuOverlayController.isAguabitConnected) {
                Platform.runLater(() -> informationLabel.setText("Download done, click the button to upload the firmware."));
            }
            else{
                Platform.runLater(() -> informationLabel.setText("Download done, connect the Agua:bit and reopen this page to upload"));
            }
        }
        else {
            Platform.runLater(() -> {alreadyDownloading = false; informationLabel.setText("Cannot reach the server, check your internet connection and try again later.");});
        }
    }

    //the actual code for copying the hex file over to the microbit
    private void uploadingFirmware(){
        alreadyUploading = true;

        char microBitDriveLetter = MenuOverlayController.driveLetter;
        Path firmwareLocation = Paths.get(pathToFirmware);
        Path microbitDrive = Paths.get(microBitDriveLetter + ":\\Aguabit-firmware.hex");
        System.out.println(microbitDrive);
        try {
            Files.copy(firmwareLocation, microbitDrive, StandardCopyOption.REPLACE_EXISTING);
        } catch (NoSuchFileException ignored){}
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {waitingAfterUploading.start(); informationLabel.setText("Upload done, the Agua:bit is ready for use.");});
    }

    //creates a 60 second "timer" after the uploading of the firmware is done, so the microbit has time to get ready for another upload again
    private void waitAfterUploading(){
        waitingAfterUploadingBoolean = true;
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        alreadyUploading = false;
        waitingAfterUploadingBoolean = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MenuOverlayController.isAguabitConnected) {
            informationLabel.setText("Click the upload button to upload the firmware.\nPress the download button to (re)download the firmware.");
        }
        else{
            informationLabel.setText("Please connect the Agua:bit and reopen this page to upload.\nPress the download button to (re)download the firmware.");}
    }

    //this code checks if a website is reachable (and thus if the user and the website are online)
    public static boolean isReachable(String urlToCheck){
        try {
            URL websiteUrl = new URL(urlToCheck);
            HttpsURLConnection connection = (HttpsURLConnection) websiteUrl.openConnection();
            connection.setRequestMethod("HEAD");
            int responsecode = connection.getResponseCode();
            return responsecode >= 200 && responsecode <= 399;
        } catch (IOException abc){
            return false;
        }
    }
}
