package org.example.Service;

import org.example.db.CarDAO;
import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.SearchCondition;
import org.example.DTO.User;
import org.example.Repository.RentalRepository;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;

import java.util.List;
import java.util.UUID;

public class CarService {
    private static CarService instance;
    public static CarService getInstance(){
        if(instance==null){
            instance = new CarService();
        }
        return instance;
    }
    private CarDAO carDAO;
    private RentalRepository rentalRepository;
    private PayService payService;
    private LoginService loginService;
    private CarService() {
        loginService = LoginService.getInstance();
        this.carDAO = CarDAO.getInstance();
        this.rentalRepository = RentalRepository.getInstance();
        this.payService = PayService.getInstance();
    }
    public void checkHasCar(){
        if(SessionManager.INSTANCE.getCar()==null){
            throw new ExitPageException();
        }
    }
    public void checkHasNoCar(){
        if(SessionManager.INSTANCE.getCar()!=null){
            throw new ExitPageException();
        }
    }
    public List<Car> getConditionCar(SearchCondition condition) {
        return carDAO.getAvailableCars(condition);
    }
    public List<Car> getAllCars() {
        List<Car> allCars = carDAO.getAllCars();
        return allCars;
    }
    public void printCarList(List<Car> carList){
        System.out.println("---- [ 전체 차량 목록 ] ----");
        for(Car car : carList) {
            String status = car.isAvailable() ? "대여 가능" : "대여 중";
            System.out.println(
                    "ID: " + car.getCarID()
                            + " | 모델: " + car.getModelName()
                            + " | 상태: " + status
            );
            System.out.println("--------------------------");
        }
    }
    public void lentCar(Car car, int rentalHours) {
        User sessionUser = SessionManager.INSTANCE.getUser();

        if (sessionUser == null) {
            System.out.println("로그인 해주세요.");
            return;
        }
        loginService.checkLicense();

        int carIdInt;
        try {
            carIdInt = Integer.parseInt(car.getCarID());
        } catch (NumberFormatException e) {
            System.out.println("차량 ID 오류");
            return;
        }

        if(car != null && car.isAvailable()) {
            String rentalID = UUID.randomUUID().toString();
            double startMileage = car.getCurrentMileage();

            Rental rental = new Rental(rentalID, sessionUser.getId(), car.getCarID(), startMileage);
            rental.setRentalHour(rentalHours);
            rentalRepository.save(rental);

            boolean updateSuccess = carDAO.updateRentedStatus(carIdInt, true);
            if (updateSuccess) {
                car.setAvailable(false);
                SessionManager.INSTANCE.setCar(car);
                SessionManager.INSTANCE.setRental(rental);
                System.out.println("차량 [" + car.getModelName() + "]가 대여되었습니다.");
            }
        }
        else {
            System.out.println("선택하신 차량은 이미 대여된 차량입니다.");
        }
    }
    public void returnCar(String payMethod) {
        Car sessionCar = SessionManager.INSTANCE.getCar();
        Rental sessionRental = SessionManager.INSTANCE.getRental();

        int carIdInt = Integer.parseInt(sessionCar.getCarID());
        Car car = carDAO.getCarById(carIdInt);

        boolean payResult = payService.processReturnPayment(payMethod);

        if (payResult) {
            carDAO.updateRentedStatus(carIdInt, false);
            car.setAvailable(true);
            sessionRental.setRentalStatus("반납 완료");
            rentalRepository.save(sessionRental);

            System.out.println("차량 [" + car.getModelName() + "]가 반납되었습니다.");

            SessionManager.INSTANCE.setCar(null);
            SessionManager.INSTANCE.setRental(null);
        } else {
            System.out.println("결제 실패로 반납 처리가 이루어지지 않았습니다.");
        }
    }
}