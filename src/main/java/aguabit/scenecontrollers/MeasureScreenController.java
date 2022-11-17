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

    public void printComms(ActionEvent event) throws IOException {
        int numberOfInput = 0;
        String receivedFromMicrobit = "";
        SerialPort microBit = SerialPort.getCommPorts()[0];
        microBit.openPort();
        microBit.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        microBit.setBaudRate(115200);
        OutputStream sendToMicroBit = microBit.getOutputStream();
        InputStream readFromMicroBit = microBit.getInputStream();

        try {
            sendToMicroBit.write('M');
            java.util.concurrent.TimeUnit.SECONDS.sleep(2);
            for (int i = 0; i < port1.length(); i++) {
                sendToMicroBit.write(port1.charAt(i));
            }
            for (int i = 0; i < port2.length(); i++) {
                sendToMicroBit.write(port2.charAt(i));
            }
            for (int i = 0; i < port3.length(); i++) {
                sendToMicroBit.write(port3.charAt(i));
            }

            sendToMicroBit.flush();
            sendToMicroBit.close();

            while(numberOfInput < 5){
                System.out.println((char) readFromMicroBit.read());
                numberOfInput++;
            }
            readFromMicroBit.close();
        } catch (Exception e) {e.printStackTrace();}
        microBit.closePort();
        System.out.println("received: " + receivedFromMicrobit);

    }
}


