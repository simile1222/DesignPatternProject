package org.example.Service;

import org.example.DTO.Car;
import org.example.SessionManager;
import org.example.db.CarDAO;

import java.util.List;

public class CarService {
    CarDAO carDAO = new CarDAO();
    private SessionManager sessionManager = SessionManager.INSTANCE;
    public CarService(){
    }
    public List<Car> showCarList(){
        return carDAO.getAllCars();
    }
    public Car getCar(String id){
        return carDAO.getCarById(Integer.parseInt(id));
    }
    public Boolean lentCar(Car car){
        if(carDAO.updateRentedStatus(car.getId(),true)){
            sessionManager.setCar(car);
            return true;
        }
        return false;
    }
    public Boolean returnCar(){
        if(!checkCar()){return false;}
        Car car = sessionManager.getCar();
        if(carDAO.updateRentedStatus(car.getId(),false)){
            sessionManager.setCar(null);
            return true;
        }else{
            System.out.println("반납중 문제가 발생했다");
            return false;
        }
    }
    public Boolean checkCar(){
        if(sessionManager.getCar()==null){
            System.out.println("대여중인 차량이 없다");
            return false;
        }
        return true;
    }


}
