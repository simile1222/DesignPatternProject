package org.example.page;

import org.example.InputUtil;
import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.Service.PayService;

public class MainPage implements Page {

    @Override
    public void showPage() {

        System.out.println("=========================================\n");
        System.out.println("  🚗    굿카(GoodCar) 차량 대여 서비스    🚗\n");
        System.out.println("     1. 단국대 학교 학생들이 창업했습니다");
        System.out.println("     2. 저희 굿카 3팀 일동은 먹튀를 하지 않습니다");
        System.out.println("     3. 정직한 차량 대여 서비스를 제공하겠습니다\n");
        System.out.println("=========================================\n");

        while (true) {
            int input = InputUtil.getInt("회원관리", "차량관리", "내정보");

            switch (input) {
                case 1 -> new LoginPage().showPage();
                case 2 -> new CarPage().showPage();
                case 3 -> new InfoPage().showPage();
                case 0 -> {
                    System.out.println("굿카 서비스를 이용해주셔서 감사합니다!");
                    return;
                }
            }
        }
    }
}
