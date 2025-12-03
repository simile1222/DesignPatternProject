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
    private LoginService loginService;
    public CarPage(){
        this.carService=CarService.getInstance();
//        this.payService=PayService.getInstance();
        this.loginService = LoginService.getInstance();
    }
    @Override
    public void showPage() {
        while(true){
            int input  = InputUtil.getInt("차량 목록보기","차량 반납하기");
            switch (input){
                case 1 -> getCarList();
                case 2-> returnCar();
                case 0-> {
                    return;
                }
            }
        }
    }

    // 차량 목록 조회
    private void getCarList() {
        SearchCondition condition = getSearchCondition();
        List<Car> carList = carService.getConditionCar(condition);

        // 조건 맞는 차량 없음
        if (carList.isEmpty()) {
            System.out.println("⚠️ 조건에 맞는 대여 가능 차량이 없습니다.");
            return;
        }

        // 번호 달린 리스트 출력 (수정된 printCarList 사용)
        carService.printCarList(carList);

        // 차량 선택
        while (true) {
            String input = InputUtil.getLine("원하는 차량 번호 입력 (0: 뒤로가기) : ");

            // 뒤로가기
            if (input.equals("0")) return;

            // 숫자인지 검증
            if (!input.matches("\\d+")) {
                System.out.println("⚠️ 숫자를 입력해주세요.");
                continue;
            }

            int num = Integer.parseInt(input);

            // 번호 범위 검증
            if (num < 1 || num > carList.size()) {
                System.out.println("⚠️ 목록에 있는 번호를 입력해주세요.");
                continue;
            }

            // 정상 선택
            Car selected = carList.get(num - 1);
            lentCar(selected);
            break;
        }
    }

    private SearchCondition getSearchCondition(){
        System.out.println("[옵션을 선택하세요]");
        String model = null;
        Boolean isRented = false;
        Double minPrice = 0.0;
        Double maxPrice = 1000000.0;
        Integer parkingId = 1;
        boolean run = true;
        while(run){
            int option=InputUtil.getInt("모델명","최소가격","최대가격","주차장위치","검색완료");
            switch (option){
                case 1-> model = InputUtil.getLine("[모델명을 입력하세요]");
                case 2-> minPrice = Double.parseDouble(InputUtil.getLine("최소가격을 입력하세요 :"));
                case 3-> maxPrice = Double.parseDouble(InputUtil.getLine("최대가격을 입력하세요 :"));
                case 4-> parkingId = Integer.parseInt(InputUtil.getLine("주차장 아이디(숫자)를 입력하세요 :"));
                case 5-> run =false;
                case 0 -> run=false;
                default-> run=false;
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
            int hour = Integer.parseInt(InputUtil.getLine("대여 시간을 입력하세요 :"));
            carService.lentCar(car,hour);
        }catch (ExitPageException e) {
            return;
        }
    }
    private void returnCar(){
        try {
            loginService.checkLogIn();
            loginService.checkLicense();
            carService.checkHasCar();
            System.out.print("결제 수단을 고르세요");
            int payment = InputUtil.getInt("KAKAO","CARD");
            String method = null;
            if(payment==1){
                method="KAKAO";
            }else if(payment==2){
                method = "CARD";
            }else{
                throw new ExitPageException();
            }
            carService.returnCar(method);
        }catch (ExitPageException e){
            return;
        }
    }
}
