package org.example.Repository;

import org.example.DTO.Car;
import org.example.DTO.SearchCondition;

import java.util.List;

public class CarRepository {

    private static CarRepository instance;

    public static CarRepository getInstance() {
        if (instance==null){
            instance = new CarRepository();
        }
        return instance;
    }

    public Boolean upsertCar(Car car){
        return true;
    }
    public List<Car> getCarList(SearchCondition condition){
        return null;
    }
}
