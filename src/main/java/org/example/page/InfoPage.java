package org.example.page;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.User;
import org.example.InputUtil;
import org.example.SessionManager;

public class InfoPage implements Page{
    private SessionManager sessionManager = SessionManager.INSTANCE;
    @Override
    public void showPage() {
        while(true){
            int input  = InputUtil.getInt("회원정보","차량정보","대여정보");
            switch (input){
                case 1 -> userInfo();
                case 2-> carInfo();
                case 3-> rentalInfo();
                case 0-> {
                    return;
                }
            }
        }
    }
    private void userInfo(){
        User user = sessionManager.getUser();
        if(user==null){
            System.out.println("로그인 되어있지 않다");
        }else{
            user.printUser();
        }
    }
    private void carInfo(){
        Car car = sessionManager.getCar();
        if(car==null){
            System.out.println("대여중인 차량이 없다");
        }else{
            car.printCar();
        }
    }
    private void rentalInfo(){
        Rental rental = sessionManager.getRental();
        if(rental==null){
            System.out.println("렌탈 정보가 없다");
        }else{
            System.out.printf("대여시간은 %d 이다\n",rental.getRentalHour());
        }
    }
}
