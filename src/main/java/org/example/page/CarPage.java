package org.example.page;

import org.example.DTO.Car;
import org.example.Exception.ExitPageException;
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
//        System.out.println("옵션을 선택하시오");
//        SearchCondition condition = new SearchCondition();
        List<Car> carList = carService.showCarList();
        String carNum = InputUtil.getLine("원하는 차량 번호 선택");
        lentCar(carList.get(Integer.parseInt(carNum)-1));
    }
    private void lentCar(Car car){
        try{
            loginService.checkLogIn();
            loginService.checkLicense();
            carService.checkHasNoCar();
            lentCar(car);
        }catch (ExitPageException e) {
            return;
        }
    }
    private void returnCar(){
        try {
            loginService.checkLogIn();
            loginService.checkLicense();
            carService.checkHasCar();
            payService.pay();
            carService.returnCar();
        }catch (ExitPageException e){
            return;
        }
    }
}
