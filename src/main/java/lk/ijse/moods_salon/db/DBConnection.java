package lk.ijse.moods_salon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moodssalon","root","1234");
    }

    public static DBConnection getInstance() throws SQLException {
        if(dbConnection == null) {
            return dbConnection = new DBConnection();
        } else {
            return dbConnection;
        }
    }
    public Connection getConnection() {
        return connection;
    }
}
