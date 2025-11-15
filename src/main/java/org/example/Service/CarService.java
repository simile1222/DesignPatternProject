package org.example.Service;

import org.example.DTO.Car;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;
import org.example.db.CarDAO;

import java.util.List;

public class CarService {
    CarDAO carDAO = new CarDAO();
    private SessionManager sessionManager = SessionManager.INSTANCE;
    public CarService(){
    }

    /**
     * Page에서 필요한 메소드 목록
     * showCarList()
     * lentCar()
     * returnCar()
     *
     * */

    /**차량을 가지고 있는지 체크*/
    public void checkHasCar(){
        if(sessionManager.getCar()==null){
            throw new ExitPageException();
        }
    }
    /** 차량을 가지고 없는지 체크*/
    public void checkHasNoCar(){
        if(sessionManager.getCar()!=null){
            throw new ExitPageException();
        }
    }


}
