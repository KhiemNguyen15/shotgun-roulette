package com.github.khiemnguyen15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHelper {
    private Connection conn;

    public void loadDatabase(Properties databaseProps) throws SQLException {
        conn = DriverManager.getConnection(databaseProps.getProperty("url"), databaseProps);
    }

    public void closeConnection() {
        try { conn.close(); } catch (Exception ignored) {}
    }
}