package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;
import org.example.Repository.CarRepository;

import java.util.List;

public class CarService {
    CarRepository carRepository = CarRepository.getInstance();
    public List<Car> showCarList(SearchCondition condition){
        return carRepository.getCarList(condition);
    }
    public Boolean lentCar(String carId){
        return null;
    }
    public Boolean returnCar(Car car){
        return null;
    }

}
