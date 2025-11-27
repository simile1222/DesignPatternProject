package org.example.Service;

import org.example.db.CarDAO;
import org.example.db.DatabaseManager;
import org.example.DTO.Car;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;
import org.example.db.CarDAO;

import java.util.List;
import java.util.Scanner;

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

    // 🔐 면허 인증 포함 대여 기능
    public void rentCar(String userId, Scanner sc) {
        if (!loginService.isLicenseVerified(userId)) {
            System.out.println("❌ 대여 불가: 면허 미인증 상태입니다.");
            return;
        }

        try {
            CarDAO dao = new CarDAO(); // ★ 수정됨
            System.out.print("대여할 차량 ID ▶ ");
            int carId = sc.nextInt();

            boolean success = dao.updateRentedStatus(carId, true);
            if (success) System.out.println("✅ 차량 대여 완료!");
            else System.out.println("❌ 차량 대여 실패");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnCar(Scanner sc) {
        try {
            CarDAO dao = new CarDAO(); // ★ 수정됨
            System.out.print("반납할 차량 ID ▶ ");
            int carId = sc.nextInt();

            boolean success = dao.updateRentedStatus(carId, false);
            if (success) System.out.println("✅ 차량 반납 완료!");
            else System.out.println("❌ 차량 반납 실패");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}