package org.example.page;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.User;
import org.example.InputUtil;
import org.example.SessionManager;

public class InfoPage implements Page {
    private SessionManager sessionManager = SessionManager.INSTANCE;

    @Override
    public void showPage() {
        while (true) {
            int input = InputUtil.getInt(
                    "회원 정보 조회",
                    "대여 중 차량 정보",
                    "렌탈 이력 조회"
            );

            switch (input) {
                case 1 -> userInfo();
                case 2 -> carInfo();
                case 3 -> rentalInfo();
                case 0 -> { return; }
            }
        }
    }

    private void userInfo() {
        User user = sessionManager.getUser();
        if (user == null) {
            System.out.println("⚠️  현재 로그인된 회원이 없습니다.\n   로그인 후 이용해주세요.");
        } else {
            System.out.println("====== 🧑‍💼 회원 정보 ======");
            user.printUser();
            System.out.println("===========================\n");
        }
    }

    private void carInfo() {
        Car car = sessionManager.getCar();
        if (car == null) {
            System.out.println("🚗 현재 대여 중인 차량이 없습니다.\n   차량 관리 메뉴에서 원하는 차량을 대여해보세요!");
        } else {
            System.out.println("====== 🚘 대여 중 차량 정보 ======");
            car.printCar();
            System.out.println("===============================\n");
        }
    }

    private void rentalInfo() {
        Rental rental = sessionManager.getRental();
        if (rental == null) {
            System.out.println("📄 등록된 렌탈 정보가 없습니다.");
        } else {
            System.out.println("======= ⏱ 렌탈 이용 정보 =======");
            System.out.printf(" - 이용 시간 : %d시간\n", rental.getRentalHour());
            System.out.println("=============================\n");
        }
    }
}
