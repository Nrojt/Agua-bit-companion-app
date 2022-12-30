package aguabit.scenecontrollers;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getDBConnection() {

        String url = "jdbc:sqlite:src/main/resources/aguabit/scenecontrollers/database/aguabit.db";

        try {
            databaseLink = DriverManager.getConnection(url);
            System.out.println("Connected to sqlite");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
