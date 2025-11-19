package org.example;

import org.example.Service.*;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LoginService loginService = new LoginService();
        CarService carService = new CarService();
        String userId = null;

        while (true) {
            System.out.println("\n=== 🚗 차량 렌트 시스템 ===");
            System.out.println("[1] 로그인");
            System.out.println("[2] 차량 목록");
            System.out.println("[3] 차량 검색");
            System.out.println("[4] 차량 대여");
            System.out.println("[5] 차량 반납");
            System.out.println("[0] 종료");
            System.out.print("선택 ▶ ");

            int input = sc.nextInt();
            sc.nextLine();

            switch (input) {
                case 1 -> {
                    System.out.print("아이디 입력 ▶ ");
                    String id = sc.nextLine();
                    System.out.print("비밀번호 입력 ▶ ");
                    String pw = sc.nextLine();
                    if (loginService.login(id, pw)) userId = id;
                }
                case 2 -> carService.showAllCars();
                case 3 -> carService.searchAvailableCars(sc);
                case 4 -> {
                    if (userId == null) {
                        System.out.println("⚠️ 로그인 후 이용 가능합니다.");
                        break;
                    }
                    carService.rentCar(userId, sc);
                }
                case 5 -> carService.returnCar(sc);
                case 0 -> {
                    System.out.println("👋 종료합니다.");
                    return;
                }
                default -> System.out.println("❌ 잘못된 입력입니다.");
            }
        }
    }
}
