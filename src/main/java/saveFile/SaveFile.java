package saveFile;

import aguabit.scenecontrollers.MeasureScreenController;

import java.io.FileOutputStream;
import java.io.PrintStream;

//different package that will probably be used for saving the settings and measurements locally
public class SaveFile {
    public static boolean menuBarSide = true;
    public static boolean darkmode = false;

    public static void saveMeasurementLocal(String measurementName, String measurementLocation, String sensor1Type, String sensor2Type, String sensor3Type, String sensor1Value, String sensor2Value, String sensor3Value, String sensor1Indication, String sensor2Indication, String sensor3Indication){

        //saves output in to a .txt file has to be linked to measure screen controller.
        FileOutputStream out;
        PrintStream p;

        try {
            // connected to "myfile.txt"
            out = new FileOutputStream("myfile.txt");
            p = new PrintStream(out);
            p.append(measurementName+"\n"+measurementLocation+"\n"+sensor1Type+"\n"+sensor2Type+"\n"+sensor3Type+"\n"+sensor1Value+"\n"+sensor2Value+"\n"+sensor3Value+"\n"); // connected it to the port which connects to the sensor value.
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println ("Error writing to file")
        }
    }
}
