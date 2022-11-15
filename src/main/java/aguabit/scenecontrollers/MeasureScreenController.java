package aguabit.scenecontrollers;

import com.fazecast.jSerialComm.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.io.InputStream;

public class MeasureScreenController {
    public void printComms(ActionEvent event) throws IOException {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setBaudRate(115200);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        try
        {
            for (int j = 0; j < 100; ++j)
                System.out.print((char)in.read());
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }

}
