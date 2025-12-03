package org.example.db;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private final Connection conn;
    private static CarDAO instance;
    public static CarDAO getInstance(){
        if(instance==null){
            instance=new CarDAO();
        }
        return instance;
    }

    private CarDAO(){
        conn = DatabaseManager.connect();
    }
    // 차량 등록 (INSERT)
    public boolean insertCar(Car car) {
        String sql = "INSERT INTO cars (model, plate_no, parking_id, price_per_hour, price_per_km, mileage, is_rented) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, car.getModelName());
            pstmt.setString(2, car.getCarPlateNumber());
            pstmt.setInt(3, car.getParkingId());
            pstmt.setDouble(4, car.getPricePerHour());
            pstmt.setDouble(5, car.getPricePerKm());
            pstmt.setInt(6, car.getMileage());
            pstmt.setInt(7, car.isAvailable() ? 1 : 0);
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
                        .carID(rs.getString("id"))
                        .modelName(rs.getString("model"))
                        .carPlateNumber(rs.getString("plate_no"))
                        .parkingId(rs.getInt("parking_id"))
                        .pricePerHour(rs.getDouble("price_per_hour"))
                        .pricePerKm(rs.getDouble("price_per_km"))
                        .mileage(rs.getInt("mileage"))
                        .isAvailable(rs.getInt("is_rented") == 1)
                        .createdAt(rs.getString("created_at"))
                        .build();
                list.add(car);
            }
        } catch (SQLException e) {
            System.out.println("❌ 차량 조회 실패: " + e.getMessage());
            return null;
        }
        return list;
    }

    // 조건 기반 차량 조회 (is_rented = 0만)
    public List<Car> getAvailableCars(SearchCondition cond) {
        List<Car> list = new ArrayList<>();

        //기본 쿼리 (대여가능한 차량만)
        StringBuilder sql = new StringBuilder("SELECT * FROM cars WHERE is_rented = 0");

        //동적 조건 추가
        if (cond.getModel() != null && !cond.getModel().isBlank()) {
            sql.append(" AND model LIKE ?");
        }
        if (cond.getParkingId() != null) {
            sql.append(" AND parking_id = ?");
        }
        if (cond.getMinPrice() != null) {
            sql.append(" AND price_per_hour >= ?");
        }
        if (cond.getMaxPrice() != null) {
            sql.append(" AND price_per_hour <= ?");
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (cond.getModel() != null && !cond.getModel().isBlank()) {
                pstmt.setString(idx++, "%"+ cond.getModel() + "%");
            }
            if (cond.getParkingId() != null) {
                pstmt.setInt(idx++, cond.getParkingId());
            }
            if (cond.getMinPrice() != null) {
                pstmt.setDouble(idx++, cond.getMinPrice());
            }
            if (cond.getMaxPrice() != null) {
                pstmt.setDouble(idx++, cond.getMaxPrice());
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Car car = Car.builder()
                        .carID(rs.getString("id"))
                        .modelName(rs.getString("model"))
                        .carPlateNumber(rs.getString("plate_no"))
                        .parkingId(rs.getInt("parking_id"))
                        .pricePerHour(rs.getDouble("price_per_hour"))
                        .pricePerKm(rs.getDouble("price_per_km"))
                        .mileage(rs.getInt("mileage"))
                        .isAvailable(true)
                        .createdAt(rs.getString("created_at"))
                        .build();
                list.add(car);
            }
            // ⚠️ 조건에 맞는 차량이 없을 경우
            if (list.isEmpty()) {
                System.out.println("⚠️ 조건에 맞는 대여 가능 차량이 없습니다.");
            }

            return list;
        } catch (SQLException e) {
            System.out.println("❌ 차량 검색 실패: " + e.getMessage());
            return null; // 예외 발생 시 null 리턴
        }

    }

    // 특정 차량 조회 (SELECT by ID)
    public Car getCarById(int id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Car.builder()
                        .carID(rs.getString("id"))
                        .modelName(rs.getString("model"))
                        .carPlateNumber(rs.getString("plate_no"))
                        .parkingId(rs.getInt("parking_id"))
                        .pricePerHour(rs.getDouble("price_per_hour"))
                        .pricePerKm(rs.getDouble("price_per_km"))
                        .mileage(rs.getInt("mileage"))
                        .isAvailable(rs.getInt("is_rented") == 1)
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
