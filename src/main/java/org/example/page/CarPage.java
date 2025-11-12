package org.example.page;

import org.example.DTO.Car;
import org.example.InputUtil;
import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.Service.PayService;
import org.example.SessionManager;

import java.util.List;

public class CarPage implements Page{

    private SessionManager sessionManager;
    private CarService carService;
    private PayService payService;
    private LoginService loginService;
    public CarPage(CarService carService,PayService payService){
        this.carService=carService;
        this.payService=payService;
        sessionManager = SessionManager.INSTANCE;
    }
    @Override
    public void showPage() {
        while(true){
            int input  = InputUtil.getInt("차량 목록보기","차량 반납하기");
            switch (input){
                case 1 -> getCarList();
//                case 2 -> lentCar();
                case 2-> returnCar();
                case 0-> {
                    return;
                }
            }
        }
    }
    private void getCarList(){
        System.out.println("옵션을 선택하시오");

//        SearchCondition condition = new SearchCondition();
        List<Car> carList = carService.showCarList();
        String carNum = InputUtil.getLine("원하는 차량 번호 선택");
        lentCar(carList.get(Integer.parseInt(carNum)-1));
    }
    private void lentCar(Car car){
        if(!loginService.checkLogIn()){return;}
        if (!loginService.checkLicense()) {
            return;}
        if(carService.checkCar()){
            System.out.println("대여중인 차량이 이미 있으면 대여가 불가하다");
            return;
        }
        carService.lentCar(car);
        System.out.println("차량 대여 성공");

    }
    private void returnCar(){
        if(!loginService.checkLogIn()){return;}

        if(!carService.checkCar()){return;}

        payService.pay();

        carService.returnCar();
    }
}
