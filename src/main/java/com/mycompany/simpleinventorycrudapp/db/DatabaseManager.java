package com.mycompany.simpleinventorycrudapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // Make sure your SQLite DB is located at "db/inventory.db" relative to your project root
    private static final String DB_URL = "jdbc:sqlite:db/inventory.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
