package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:car_rental.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
//            System.out.println("✅ SQLite 연결 성공!");
            return conn;
        } catch (SQLException e) {
//            System.out.println("❌ DB 연결 실패: " + e.getMessage());
            return null;
        }
    }
}
