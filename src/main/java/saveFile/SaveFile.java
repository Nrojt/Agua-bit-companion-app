package saveFile;

import aguabit.scenecontrollers.MeasureScreenController;

import java.io.FileOutputStream;
import java.io.PrintStream;

//different package that will probably be used for saving the settings and measurements locally
public class SaveFile {
    public static boolean menuBarSide = true;
    public static boolean darkmode = false;

    public static void main(String args[]) {

        //saves output in to a .txt file has to be linked to measure screen controller.
        FileOutputStream out;
        PrintStream p;

        try {
            // connected to "myfile.txt"
            out = new FileOutputStream("myfile.txt");
            p = new PrintStream(out);
            p.append(MeasureScreenController.port1 + "\n" + MeasureScreenController.port2 + "\n" + MeasureScreenController.port3); // connected it to the port which connects to the sensor value.

            p.close();
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println ("Error writing to file")
        }
    }
}
