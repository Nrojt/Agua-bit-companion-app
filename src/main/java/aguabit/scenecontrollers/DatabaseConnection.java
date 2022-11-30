package aguabit.scenecontrollers;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getDBConnection() {

        String url = "jdbc:sqlite:C:\\Users\\Gebruiker\\IdeaProjects\\JavaFX-menu-test-Challenge\\src\\main\\java\\db\\aguabit.db";

        try {

            databaseLink = DriverManager.getConnection(url);
            System.out.println("Connected to sqlite");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
