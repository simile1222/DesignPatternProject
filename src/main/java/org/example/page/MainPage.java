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
            int input = InputUtil.getInt("회원관리","차량관리","내정보");
            switch (input) {
                case 1 -> handleLogin();
                case 2 -> showCars();
                case 3 -> showInfo();
                case 0 -> {
                    return;
                }
            }
            page.showPage();
        }
    }
    public void handleLogin(){
        page = new LoginPage();
    }
    public void showCars(){
        page = new CarPage();
    }
    public void showInfo(){
        page = new InfoPage();
    }
}
