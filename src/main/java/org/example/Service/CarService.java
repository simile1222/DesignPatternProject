package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;
import org.example.Repository.CarRepository;

import java.util.List;

public class CarService {
    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    
    public List<Car> showCarList(SearchCondition condition) {
        return carRepository.getCarList(condition);
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
    public void rent(String carID) {
        Car car = carRepository.findByID(carID);
        if(car == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
        } else if(car.isAvailable()) {
            car.setAvailable(false);
            carRepository.save(car);
            System.out.println("차량 [" + carID + "] 대여되었습니다.");
        } else {
            System.out.println("차량 [" + carID + "]는 이미 대여된 차량입니다.");
        }
    }
    public void returnCar(String carID) {
        Car car = carRepository.findByID(carID);
        if(car == null) {
            System.out.println("차량 정보가 존재하지 않습니다.");
        } else if (!car.isAvailable()) {
            car.setAvailable(true);
            carRepository.save(car);
            System.out.println("차량 [" + carID + "]가 반납되었습니다.");
        } else {
            System.out.println("차량 [" + carID + "]는 대여 중인 상태가 아니므로 반납할 수 없습니다.");
        }
    }

}
