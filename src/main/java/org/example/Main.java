package org.example;

import org.example.db.DatabaseManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.connect()) {
            if (conn == null) {
                System.out.println("❌ 연결 실패 (Connection is null)");
                return;
            }

            // 연결 성공했으면 간단히 쿼리 테스트
            System.out.println("✅ DB 연결 테스트 성공");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            System.out.println("📋 현재 테이블 목록:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

