package db;

import java.sql.*;

public class DbConnectionManager {
    private static DbConnectionManager dbConnectionManager;
    private String url = "jdbc:sqlite:traffic_data.db";

    private DbConnectionManager() {}

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Fail to create a DB connection  : " + ex.getMessage());
        }


        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DbConnectionManager getInstance() throws SQLException {
        if (dbConnectionManager == null) {
            dbConnectionManager = new DbConnectionManager();
        } else if (dbConnectionManager.getConnection().isClosed()) {
            dbConnectionManager = new DbConnectionManager();
        }
        return dbConnectionManager;
    }
}

