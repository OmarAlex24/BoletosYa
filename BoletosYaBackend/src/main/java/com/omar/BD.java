package com.omar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BD {
    private static BD instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/boletos_ya_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1324";

    private BD() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static BD getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new BD();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
