package org.example;

import org.example.Repository.UserRepository;
import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.page.LoginPage;
import org.example.page.Page;

import java.util.Scanner;

public class Demo {
    static Page page = null;
    public static void main(String[] args) {
        while(true){
            int input = InputUtil.getInt(InputUtil.makeQuestion("로그인","차량 목록","차량 대여","반납"));
            switch (input) {
                case 1 -> handleLogin();
                case 2 -> showCars();
                case 3 -> handleRent();
                case 4 -> handleReturn();
                case 0 -> System.exit(0);
            }
            page.showPage();
        }
    }
    public static void handleLogin(){
        page = new LoginPage(new LoginService());
    }
    public static void showCars(){}
    public static void handleRent(){}
    public static void handleReturn(){}
}
