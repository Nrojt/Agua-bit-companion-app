package aguabit.helpscripts;

import javafx.scene.paint.Color;

public class MeasurementIndications {
    //this code gives an indication of the measurement values
    public static String getMeasurementIndication(String sensorType, String sensorValue){
        String indication = "";
        Color colour = Color.BLACK;

        if(!sensorValue.equals("Unknown") && !sensorValue.equals("Empty")) {
            if (sensorType.equals("PH-Sensor")) {
                if (Double.parseDouble(sensorValue) > 6.5 && Double.parseDouble(sensorValue) <= 8.5) {
                    indication = "Safe";
                    colour = Color.rgb(19, 255, 2);
                } else if (Double.parseDouble(sensorValue) > 8.5 && Double.parseDouble(sensorValue) < 10) {
                    indication = "Warning";
                    colour = Color.rgb(255, 234, 2);
                } else if (Double.parseDouble(sensorValue) < 6.5 && Double.parseDouble(sensorValue) > 4) {
                    indication = "Warning";
                    colour = Color.rgb(255, 234, 2);
                } else{
                    indication = "Unsafe";
                    colour = Color.rgb(255, 2, 2);
                }
            } else if (sensorType.equals("Temperature sensor")) {
                if (Double.parseDouble(sensorValue) > 10 && Double.parseDouble(sensorValue) <= 20) {
                    indication = "Safe";
                    colour = Color.rgb(19, 255, 2);
                } else if (Double.parseDouble(sensorValue) > 70 && Double.parseDouble(sensorValue) <= 100) {
                    indication = "Warning";
                    colour = (Color.rgb(255, 234, 2));
                } else if (Double.parseDouble(sensorValue) > 20 && Double.parseDouble(sensorValue) <= 70) {
                    indication = "Unsafe";
                    colour = Color.rgb(255, 2, 2);
                } else if (Double.parseDouble(sensorValue) <= 10) {
                    indication = "Unsafe";
                    colour = Color.rgb(255, 2, 2);
                }
            } else {
                indication = "Unknown";
                colour = Color.rgb(0, 0, 0);
            }
        } else {
            indication = "Unknown";
            colour = Color.rgb(0, 0, 0);
        }
        return indication+":"+colour;
    }
}
