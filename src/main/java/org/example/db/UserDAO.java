package org.example.db;

import org.example.DTO.User;
import java.sql.*;

public class UserDAO {
    private final Connection conn;

    public UserDAO() {
        conn = DatabaseManager.connect();
    }

    // 회원 등록 (CREATE)
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (id, password_hash, license_verified) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setInt(3, user.isLicenseVerified() ? 1 : 0);
            pstmt.executeUpdate();
            System.out.println("✅ 회원 등록 완료: " + user.getId());
            return true;
        } catch (SQLException e) {
            System.out.println("❌ 회원 등록 실패: " + e.getMessage());
            return false;
        }
    }

    // 회원 조회 (READ)
    public User getUserById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return User.builder()
                        .id(rs.getString("id"))
                        .passwordHash(rs.getString("password_hash"))
                        .licenseVerified(rs.getInt("license_verified") == 1)
                        .createdAt(rs.getString("created_at"))
                        .build();
            }
        } catch (SQLException e) {
            System.out.println("❌ 회원 조회 실패: " + e.getMessage());
        }
        return null;
    }

    // 3️⃣ 면허 인증 상태 갱신 (UPDATE)
    public boolean updateLicense(String id, boolean verified) {
        String sql = "UPDATE users SET license_verified = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, verified ? 1 : 0);
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ 면허 상태 변경 완료 (" + id + " → " + verified + ")");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ 면허 상태 변경 실패: " + e.getMessage());
        }
        return false;
    }

    // 회원 삭제 (DELETE)
    public boolean deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ 회원 삭제 완료: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ 회원 삭제 실패: " + e.getMessage());
        }
        return false;
    }
}
