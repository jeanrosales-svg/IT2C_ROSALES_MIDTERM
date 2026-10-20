package com.rosales.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the SQLite connection and schema setup.
 * All JDBC/SQL logic lives in the dao package.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /** Creates the table if it does not exist yet. */
    public static void initializeSchema() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medicine (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    brand_name TEXT    NOT NULL,
                    category   TEXT    NOT NULL
                               CHECK (category IN ('ANTIBIOTIC', 'PAINKILLER', 'VITAMIN')),
                    price      REAL    NOT NULL CHECK (price >= 0),
                    stock      INTEGER NOT NULL CHECK (stock >= 0)
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
