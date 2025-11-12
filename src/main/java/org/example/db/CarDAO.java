package org.example.db;

import org.example.DTO.Car;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private final Connection conn;


    public CarDAO(){
        conn = DatabaseManager.connect();
    }
    // 차량 등록 (INSERT)
    public boolean insertCar(Car car) {
        String sql = "INSERT INTO cars (model, plate_no, parking_id, price_per_hour, price_per_km, mileage, is_rented) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, car.getModel());
            pstmt.setString(2, car.getPlateNo());
            pstmt.setInt(3, car.getParkingId());
            pstmt.setDouble(4, car.getPricePerHour());
            pstmt.setDouble(5, car.getPricePerKm());
            pstmt.setInt(6, car.getMileage());
            pstmt.setInt(7, car.isRented() ? 1 : 0);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ 차량 등록 실패: " + e.getMessage());
            return false;
        }
    }

    // 전체 차량 조회 (SELECT *)
    public List<Car> getAllCars() {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Car car = Car.builder()
                        .id(rs.getInt("id"))
                        .model(rs.getString("model"))
                        .plateNo(rs.getString("plate_no"))
                        .parkingId(rs.getInt("parking_id"))
                        .pricePerHour(rs.getDouble("price_per_hour"))
                        .pricePerKm(rs.getDouble("price_per_km"))
                        .mileage(rs.getInt("mileage"))
                        .isRented(rs.getInt("is_rented") == 1)
                        .createdAt(rs.getString("created_at"))
                        .build();
                list.add(car);
            }
        } catch (SQLException e) {
            System.out.println("❌ 차량 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // 특정 차량 조회 (SELECT by ID)
    public Car getCarById(int id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Car.builder()
                        .id(rs.getInt("id"))
                        .model(rs.getString("model"))
                        .plateNo(rs.getString("plate_no"))
                        .parkingId(rs.getInt("parking_id"))
                        .pricePerHour(rs.getDouble("price_per_hour"))
                        .pricePerKm(rs.getDouble("price_per_km"))
                        .mileage(rs.getInt("mileage"))
                        .isRented(rs.getInt("is_rented") == 1)
                        .createdAt(rs.getString("created_at"))
                        .build();
            }
        } catch (SQLException e) {
            System.out.println("❌ 차량 조회 실패: " + e.getMessage());
        }
        return null;
    }

    // 대여 상태 업데이트 (UPDATE is_rented)
    public boolean updateRentedStatus(int carId, boolean rented) {
        String sql = "UPDATE cars SET is_rented = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rented ? 1 : 0);
            pstmt.setInt(2, carId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ 대여 상태 변경 실패: " + e.getMessage());
            return false;
        }
    }

    // 차량 삭제 (DELETE)
    public boolean deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ 차량 삭제 실패: " + e.getMessage());
            return false;
        }
    }
}
