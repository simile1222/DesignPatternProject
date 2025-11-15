package org.example.page;

import org.example.InputUtil;
import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.Service.PayService;

public class MainPage implements Page{
    private Page page;
    @Override
    public void showPage() {
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
    public void handleLogin(){
        page = new LoginPage(new LoginService());
    }
    public void showCars(){
        page = new CarPage(new CarService(),new PayService());
    }
}
