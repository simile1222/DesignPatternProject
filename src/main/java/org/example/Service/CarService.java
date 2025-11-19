package org.example.Service;

import org.example.db.*;
import org.example.DTO.Car;
import org.example.DTO.SearchCondition;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CarService {
    private final LoginService loginService = new LoginService();

    // 전체 차량 출력
    public void showAllCars() {
        try (Connection conn = DatabaseManager.connect()) {
            CarDAO dao = new CarDAO(conn);
            List<Car> cars = dao.getAllCars();
            System.out.println("\n🚗 [전체 차량 목록]");
            for (Car c : cars) {
                String status = c.isRented() ? "대여중" : "대여가능";
                System.out.printf("[%d] %s (%s) - %,d원/시간 - %s\n",
                        c.getId(), c.getModel(), c.getPlateNo(),
                        (int)c.getPricePerHour(), status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔍 조건 기반 차량 검색
    public void searchAvailableCars(Scanner sc) {
        try (Connection conn = DatabaseManager.connect()) {
            CarDAO dao = new CarDAO(conn);
            SearchCondition cond = new SearchCondition();

            System.out.println("\n🔎 [대여 가능 차량 검색]");
            System.out.print("모델명 입력 (없으면 Enter): ");
            String model = sc.nextLine();
            if (!model.isBlank()) cond.setModel(model);

            System.out.print("주차장 ID 입력 (없으면 Enter): ");
            String parkingInput = sc.nextLine();
            if (!parkingInput.isBlank()) cond.setParkingId(Integer.parseInt(parkingInput));

            System.out.print("최소 요금 입력 (없으면 Enter): ");
            String minInput = sc.nextLine();
            if (!minInput.isBlank()) cond.setMinPrice(Double.parseDouble(minInput));

            System.out.print("최대 요금 입력 (없으면 Enter): ");
            String maxInput = sc.nextLine();
            if (!maxInput.isBlank()) cond.setMaxPrice(Double.parseDouble(maxInput));

            List<Car> result = dao.getAvailableCars(cond);

            if (result == null) {
                System.out.println("❌ DB 오류 발생");
            } else if (result.isEmpty()) {
                System.out.println("⚠️ 조건에 맞는 차량이 없습니다.");
            } else {
                System.out.println("\n🚗 [검색 결과]");
                for (Car c : result) {
                    System.out.printf("[%d] %s (%s) - %,d원/시간 - 주차장: %d\n",
                            c.getId(), c.getModel(), c.getPlateNo(),
                            (int)c.getPricePerHour(), c.getParkingId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 대여 기능 (면허 인증 포함)
    public void rentCar(String userId, Scanner sc) {
        if (!loginService.isLicenseVerified(userId)) {
            System.out.println("❌ 대여 불가: 면허 미인증 상태입니다.");
            return;
        }

        try (Connection conn = DatabaseManager.connect()) {
            CarDAO dao = new CarDAO(conn);
            System.out.print("대여할 차량 ID ▶ ");
            int carId = sc.nextInt();
            boolean success = dao.updateRentedStatus(carId, true);
            if (success) System.out.println("✅ 차량 대여 완료!");
            else System.out.println("❌ 차량 대여 실패");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnCar(Scanner sc) {
        try (Connection conn = DatabaseManager.connect()) {
            CarDAO dao = new CarDAO(conn);
            System.out.print("반납할 차량 ID ▶ ");
            int carId = sc.nextInt();
            boolean success = dao.updateRentedStatus(carId, false);
            if (success) System.out.println("✅ 차량 반납 완료!");
            else System.out.println("❌ 차량 반납 실패");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
