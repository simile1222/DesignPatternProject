package org.example.Service;

import org.example.db.CarDAO;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.SearchCondition;
import org.example.DTO.User;
import org.example.Repository.CarRepository;
import org.example.Repository.RentalRepository;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;

import java.util.List;
import java.util.UUID;

public class CarService {
    private CarRepository carRepository;
    private RentalRepository rentalRepository;
    private PayService payService;
    public void checkHasCar(){
        if(sessionManager.getCar()==null){
            throw new ExitPageException();
        }
    }
    public void checkHasNoCar(){
        if(sessionManager.getCar()!=null){
            throw new ExitPageException();
        }
    }
    public CarService(CarRepository carRepository, RentalRepository rentalRepository, PayService payService) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
        this.payService = payService;
    }
    public List<Car> showCarList(SearchCondition condition) {
        return carRepository.getCarList(condition);
    }
    public List<Car> getLendableCars() {
        return carRepository.findLendableCars();
    }
    public void getAllCars() {
        List<Car> allCars = carRepository.findAll();
        System.out.println("---- [ 전체 차량 목록 ] ----");
        for(Car car : allCars) {
            String status = car.isAvailable() ? "대여 가능" : "대여 중";
            System.out.println(
                "ID: " + car.getCarID()
                + " | 모델: " + car.getModelName()
                + " | 상태: " + status
            );
            System.out.println("--------------------------");
        }
    }
    public void rent(int rentalHours) {
        User sessionUser = SessionManager.INSTANCE.getUser();
        Car sessionCar = SessionManager.INSTANCE.getCar();

        if (sessionUser == null) {
            System.out.println("로그인 정보가 없습니다.");
            return;
        }
        if (sessionCar == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
            return;
        }

        if (sessionUser.getLicenseNumber() == null) {
            System.out.println("❌ 대여 불가: 면허 미인증 상태입니다.");
        }

        Car car = carRepository.findByID(sessionCar.getCarID());
        if(car.isAvailable()) {
            String rentalID = UUID.randomUUID().toString();
            double startMileage = car.getCurrentMileage();

            Rental rental = new Rental(rentalID, sessionUser.getUserID(), sessionCar.getCarID(), startMileage);
            rentalRepository.save(rental);

            car.setAvailable(false);
            carRepository.save(car);

            SessionManager.INSTANCE.setRental(rental);
            System.out.println("차량 [" + sessionCar.getCarID() + "]가 대여되었습니다.");
        } else {
            System.out.println("차량 [" + sessionCar.getCarID() + "]는 이미 대여된 차량입니다.");
        }
    }
    public void returnCar(double finalMileage, String paymethod) {
        Car sessionCar = SessionManager.INSTANCE.getCar();
        Rental sessionRental = SessionManager.INSTANCE.getRental();
        String targetCarID = (sessionCar != null) ? sessionCar.getCarID() : null;

        if(targetCarID == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
        }
        Car car = carRepository.findByID(targetCarID);
        Rental rental = rentalRepository.findActiveRentalByCarID(targetCarID);
        double ratePerKm = 150.0;
        boolean payResult = payService.processReturnPayment(rental, car, finalMileage, ratePerKm, paymethod);
        if (payResult) {
            car.setAvailable(true);
            car.setCurrentMileage(finalMileage);
            carRepository.save(car);

            rental.setEndMileage(finalMileage);
            rental.setRentalStatus("반납 완료");
            rentalRepository.save(rental);

            System.out.println("차량 [" + car.getModelName() + "]가 반납되었습니다.");

            SessionManager.INSTANCE.setCar(null);
            SessionManager.INSTANCE.setRental(null);
        } else {
            System.out.println("결제 실패로 반납 처리가 이루어지지 않았습니다.");
        }
    }
}