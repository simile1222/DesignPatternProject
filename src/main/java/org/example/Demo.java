package org.example;

import org.example.Repository.UserRepository;
import org.example.Service.CarService;
import org.example.Service.LoginService;

import java.util.Scanner;

public class Demo {
    private static Scanner sc = new Scanner(System.in);
    private static CarService carService;
    private static LoginService loginService;

    public static void main(String[] args) {
        loginService = new LoginService();
        while(true){
            System.out.println("[1] 로그인 [2] 차량 목록 [3] 차량 대여 [4] 반납 [0] 종료");
            int input = sc.nextInt();
            sc.nextLine();
            switch (input) {
                case 1 -> handleLogin();
                case 2 -> showCars();
                case 3 -> handleRent();
                case 4 -> handleReturn();
                case 0 -> System.exit(0);
            }

        }
    }
    public static void handleLogin(){}
    public static void showCars(){}
    public static void handle
}