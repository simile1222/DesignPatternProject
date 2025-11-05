package org.example;

import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.Service.PayService;
import org.example.page.CarPage;
import org.example.page.LoginPage;
import org.example.page.Page;


public class Demo {
    static Page page = null;
    public static void main(String[] args) {
        while(true){
            int input = InputUtil.getInt("회원정보","차량정보");
            switch (input) {
                case 1 -> handleLogin();
                case 2 -> showCars();
                case 0 -> {
                    return;
                }
            }
            page.showPage();
        }
    }
    public static void handleLogin(){
        page = new LoginPage(new LoginService());
    }
    public static void showCars(){
        page = new CarPage(new CarService(),new PayService());
    }
}