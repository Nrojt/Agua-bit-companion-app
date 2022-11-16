package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.*;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MeasureScreenController {
    public static double temperature = 0.0;
    public static double port1Value = 0.00;
    public static double port2Value = 0.00;
    public static double port3Value = 0.00;
    public static String port1 = "PH";
    public static String port2 = "TP";
    public static String port3 = "EM";
    public static String okMessage = "";

    public void printComms(ActionEvent event) throws IOException {
        //this chrashes the program somehow, idk why
        SerialPort microBit = SerialPort.getCommPorts()[0];

        microBit.setBaudRate(115200);
        microBit.openPort();

        try {
            OutputStream sendToMicroBit = microBit.getOutputStream();
            sendToMicroBit.write('M');
            sendToMicroBit.flush();

            for (int i = 0; i < port1.length(); i++) {
                sendToMicroBit.write(port1.charAt(i));
            }
            for (int i = 0; i < port2.length(); i++) {
                sendToMicroBit.write(port2.charAt(i));
            }
            for (int i = 0; i < port3.length(); i++) {
                sendToMicroBit.write(port3.charAt(i));
            }
            sendToMicroBit.close();

            InputStream readFromMicroBit = microBit.getInputStream();
            for (int j = 0; j < 250; j++) {
                System.out.print((char)readFromMicroBit.read());
            }
            readFromMicroBit.close();
        } catch (Exception e) {e.printStackTrace();}
        microBit.closePort();

    }
}


