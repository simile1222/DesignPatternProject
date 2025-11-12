package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.SearchCondition;
import org.example.Repository.CarRepository;
import org.example.Repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarService {
    private CarRepository carRepository;
    private PayService payService;

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
    public void rent(String carID, String userID) {
        Car car = carRepository.findByID(carID);
        if(car == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
        } else if(car.isAvailable()) {
            String rentalID = UUID.randomUUID().toString();
            double startMileage = car.getCurrentMileage();
            Rental rental = new Rental(rentalID, userID, carID, startMileage);
            rentalRepository.save(rental);
            car.setAvailable(false);
            carRepository.save(car);
            System.out.println("차량 [" + carID + "]가 대여되었습니다.");
        } else {
            System.out.println("차량 [" + carID + "]는 이미 대여된 차량입니다.");
        }
    }
    public void returnCar(String carID, double finalMileage) {
        Car car = carRepository.findByID(carID);
        if(car == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
        } else if {
            System.out.println("차량 [" + carID + "]는 대여 중인 상태가 아니므로 반납할 수 없습니다.");
        }
        Optional<Rental> activeRentalOpt = rentalRepository.findActiveRentalByCarID(carID);
        Rental rental = activeRentalOpt.get();
        double ratePerKm = 150.0;
        boolean payResult = payService.processReturnPayment(rental, car, finalMileage, ratePerKm);
        if (payResult) {
            car.setAvailable(true);
            car.setCurrentMileage(finalMileage);
            carRepository.save(car);
            rental.setReturnDate(LocalDateTime.now());
            rental.setEndMileage(finalMileage);
            rental.setRentalStatus("반납 완료");
            rentalRepository.save(rental);
            System.out.println("차량 [" + carID + "]가 반납되었습니다.");
        } else {
            System.out.println("결제 실패로 반납 처리가 이루어지지 않았습니다.");
        }
    }

}
