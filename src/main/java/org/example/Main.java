package org.example;

import org.example.DTO.Car;
import org.example.db.CarDAO;
import org.example.db.DatabaseManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CarDAO carDAO = CarDAO.getInstance();
        List<Car> carList = carDAO.getAllCars();
        for(Car c : carList){
            c.setAvailable(true);
            carDAO.updateRentedStatus(Integer.parseInt(c.getCarID()),false);
        }
    }
}

