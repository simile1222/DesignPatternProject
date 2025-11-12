package org.example.page;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;
import org.example.DTO.User;
import org.example.InputUtil;
import org.example.Service.CarService;
import org.example.Service.PayService;
import org.example.SessionManager;

import java.util.List;

public class CarPage implements Page{

    private SessionManager sessionManager;
    private CarService carService;
    private PayService payService;
    public CarPage(CarService carService,PayService payService){
        this.carService=carService;
        this.payService=payService;
        sessionManager = SessionManager.INSTANCE;
    }
    @Override
    public void showPage() {
        while(true){
            int input  = InputUtil.getInt("차량 목록보기","차량 대여하기","차량 반납하기");
            switch (input){
                case 1 -> getCarList();
                case 2 -> lentCar();
                case 3-> returnCar();
                case 0-> {
                    return;
                }
            }
        }
    }
    private void getCarList(){
        System.out.println("옵션을 선택하시오");

        SearchCondition condition = new SearchCondition();
        List<Car> carList = carService.showCarList(condition);
    }
    private void lentCar(){
        User user = sessionManager.getUser();
        if(user ==null){
            System.out.println("로그인이 필요한 서비스");
            return;
        }
        if (user.isLicenseVerified()) {
            System.out.println("면허가 필요한 서비스");
            return;
        }
        if(sessionManager.getCar()!=null){
            System.out.println("대여중인 차량이 이미 있으면 대여가 불가하다");
            return;
        }
        String carId = InputUtil.getLine("차량 번호를 입력하시오");
        if(!carService.lentCar(carId)){
            System.out.println("대여 오류 발생");
            return;
        }
        sessionManager.setCar(carService.getCar(carId));

    }
    private void returnCar(){
        User user = sessionManager.getUser();
        if(user ==null){
            System.out.println("로그인이 필요한 서비스");
            return;
        }
        Car car = sessionManager.getCar();
        if(car ==null){
            System.out.println("대여중인 차량이 없다");
            return;
        }

        payService.pay();

        carService.returnCar(car);
        sessionManager.setCar(null);
    }
}
