package aguabit.scenecontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.SplittableRandom;

public class DatabaseScreenController implements Initializable {
    @FXML
    private ListView<String> DatabaseScreen;

    public void initialize(URL location, ResourceBundle resources){
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getDBConnection();

        String connectQuery = "SELECT user_id, username FROM user";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {

                String user_id = queryOutput.getString("user_id");
                String username = queryOutput.getString("username");
                String listOut = user_id + "\"" +username + "\"";

                DatabaseScreen.getItems().add(listOut);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}