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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.*;


import javax.swing.*;

public class UpdateScreenController implements Initializable {
    public static Thread downloadingFirmware;
    public static Thread uploadingFirmware;
    private final String pathToDocumentFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private final String pathToFirmware = pathToDocumentFolder + "/AguaBit/Aguabit-firmware.hex";
    private final String urlToFirmware = "https://github.com/Nrojt/AguaBit-firmware/releases/download/v1.0.1/Aguabit-firmware.hex";
    @FXML
    private Label notificationLabel = new Label();
    public void copyToMicroBit(ActionEvent e){
        if (!new File(pathToFirmware).isFile()) {
                notificationLabel.setText("Downloading firmware, please wait.");
                downloadingFirmware = new Thread(this::downloadFiles);
                downloadingFirmware.start();
            }
        else if(MenuOverlayController.isAguabitConnected){
            notificationLabel.setText("Uploading firmware to Agua:bit, please wait.");
            uploadingFirmware = new Thread(this::setUploadingFirmware);
            uploadingFirmware.start();
        }
        else{
            notificationLabel.setText("Please connect the Agua:bit and reopen this page to upload.");
        }
        //notificationLabel.setText("Click the button to update the firmware");
    }

    public void downloadButton(){
        notificationLabel.setText("Downloading firmware, please wait.");
        downloadingFirmware = new Thread(this::downloadFiles);
        downloadingFirmware.start();
    }
    private void downloadFiles(){
        try {
            if(isReachable(urlToFirmware)) {
                try {
                    FileUtils.copyURLToFile(new URL(urlToFirmware), new File(pathToFirmware), 5000, 5000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (MenuOverlayController.isAguabitConnected) {
                    Platform.runLater(() -> notificationLabel.setText("Download done, click the button to upload the firmware."));
                }

            }
            else {
                Platform.runLater(() -> notificationLabel.setText("Cannot reach the server, check your internet connection and try again later."));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUploadingFirmware(){
        char microBitDriveLetter = MenuOverlayController.driveDetector.getRemovableDevices().toString().charAt(32);
        Path firmwareLocation = Paths.get(pathToFirmware);
        Path microbitDrive = Paths.get(microBitDriveLetter + ":\\Aguabit-firmware.hex");
        System.out.println(microbitDrive);
        try {
            Files.copy(firmwareLocation, microbitDrive, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> notificationLabel.setText("Upload done, the Agua:bit is ready for use."));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MenuOverlayController.isAguabitConnected) {
            notificationLabel.setText("Click the upload button to upload the firmware.\nPress the download button to (re)download the firmware.");
        }
        else{notificationLabel.setText("Please connect the Agua:bit and reopen this page to upload.\nPress the download button to (re)download the firmware.");}
    }

    //this code checks if website is reachable, but its old and gives errors, but works for now. Stolen from https://stackoverflow.com/questions/67845222/easy-way-to-check-if-a-link-is-reachable-or-not-from-a-java-aplication
    public static boolean isReachable(String url) throws IOException{
        boolean isReachable = true;
        try (CloseableHttpClient httpClient = HttpClients.custom().setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build())
        {
            HttpHead request = new HttpHead(url);
            CloseableHttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 404) {
                System.out.println("URL " + url + " Not found");
                isReachable = false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            isReachable = false;
        }

        return isReachable;
    }
}
