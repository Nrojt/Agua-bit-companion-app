package saveFile;

import java.io.FileOutputStream;
import java.io.PrintStream;
//different package that will probably be used for saving the settings and measurements locally
public class SaveFile {
    public static boolean menuBarSide = true;

}

class FileOutput {

    public static void main(String args[])
    {
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
            // Create a new file output stream
            // connected to "myfile.txt"
            out = new FileOutputStream("myfile.txt");
            // Connect print stream to the output stream
            p = new PrintStream( out );
            p.append ("This is written to a file");

            p.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //System.err.println ("Error writing to file");
        }
    }
}