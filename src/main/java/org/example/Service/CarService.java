package org.example.Service;

import org.example.DTO.Car;
import org.example.db.CarDAO;

import java.util.List;

public class CarService {
    CarDAO carDAO = new CarDAO();
    public List<Car> showCarList(){
        return carDAO.getAllCars();
    }
    public Car getCar(String id){
        return carDAO.getCarById(Integer.parseInt(id));
    }
    public Boolean lentCar(String carId){
        return carDAO.updateRentedStatus(Integer.parseInt(carId),true);
    }
    public Boolean returnCar(Car car){
        return carDAO.updateRentedStatus(car.getId(),false);
    }


}
