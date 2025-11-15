package org.example.page;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;
import org.example.Exception.ExitPageException;
import org.example.InputUtil;
import org.example.Service.CarService;
import org.example.Service.LoginService;
import org.example.Service.PayService;
import org.example.SessionManager;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class CarPage implements Page{

    private CarService carService;
    private PayService payService;
    private LoginService loginService;
    public CarPage(CarService carService,PayService payService,LoginService loginService){
        this.carService=carService;
        this.payService=payService;
        this.loginService = loginService;
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
        SearchCondition condition = getSearchCondition();
        List<Car> carList = carService.showCarList();
        String carNum = InputUtil.getLine("원하는 차량 번호 선택");
        lentCar(carList.get(Integer.parseInt(carNum)-1));
    }
    private SearchCondition getSearchCondition(){
        System.out.println("옵션을 선택하시오");
        String model = null;
        Boolean isRented = false;
        Double minPrice = 0.0;
        Double maxPrice = 1000000.0;
        Integer parkingId = 1;
        boolean run = true;
        while(run){
            int option=InputUtil.getInt("모델명","대여가능 여부","최소가격","최대가격","주차장위치");
            switch (option){
                case 1: model = InputUtil.getLine("모델명을 입력하시오");
                case 2: isRented = InputUtil.getLine("대여가능한 차량을 원하면 1 아니면 2를 입력하시오").equals("1");
                case 3 : minPrice = Double.parseDouble(InputUtil.getLine("최소갑격을 입력하시오"));
                case 4: maxPrice = Double.parseDouble(InputUtil.getLine("최대가격을 입력하시오"));
                case 5: parkingId = Integer.parseInt(InputUtil.getLine("주차장 아이디를 입력하시오"));
                case 0 : run=false;
                default: run=false;
            }
        }
        return SearchCondition.builder()
                .model(model)
                .isRented(isRented)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .parkingId(parkingId)
                .build();
    }
    private void lentCar(Car car){
        try{
            loginService.checkLogIn();
            loginService.checkLicense();
            carService.checkHasNoCar();
            loginService.lentCar(car);
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
