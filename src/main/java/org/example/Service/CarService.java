package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.DTO.SearchCondition;
import org.example.Repository.RentalRepository;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;
import org.example.db.CarDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarService {

    private SessionManager sessionManager = SessionManager.INSTANCE;

    private RentalRepository rentalRepository;
    private PayService payService;
    private CarDAO carDAO;

    public CarService() {
    }

    public CarService(CarDAO carDAO, RentalRepository rentalRepository, PayService payService) {
        this.carDAO = carDAO;
        this.rentalRepository = rentalRepository;
        this.payService = payService;
    }

    public List<Car> showCarList(){
        List<Car> carList = carDAO.getAllCars();
        printCarList(carList);
        return carList;
    }
    private void printCarList(List<Car> carList){
        for (Car c : carList){
            System.out.printf("[Model] : %s ",c.getModel());
            System.out.printf("[PricePerHour] : %f ",c.getPricePerHour());
            System.out.printf("[Id] : %s ",c.getId());
        }
    }

    /** 차량을 가지고 있는지 체크 */
    public void checkHasCar() {
        if (sessionManager.getCar() == null) {
            throw new ExitPageException();
        }
    }

//    public List<Car> getLendableCars() {
//        return carRepository.findLendableCars();
//    }
//
//    public void getAllCars() {
//        List<Car> allCars = carRepository.findAll();
//        System.out.println("---- [ 전체 차량 목록 ] ----");
//
//        for (Car car : allCars) {
//            String status = car.isAvailable() ? "대여 가능" : "대여 중";
//            System.out.println(
//                    "ID: " + car.getCarID()
//                            + " | 모델: " + car.getModelName()
//                            + " | 상태: " + status
//            );
//            System.out.println("--------------------------");
//        }
//    }
    public void lentCar(Car car){
        if(carDAO.updateRentedStatus(car.getId(),true)){
            System.out.printf("차량 %s 가 대여되었다",car.getPlateNo());
        }else {
            System.out.println("차량이 대여되지 않았다");
        }
    }
    public void returnCar(){
        Car car = sessionManager.getCar();
        if(carDAO.updateRentedStatus(car.getId(),false)){
            System.out.println("차량이 반납되었다");
        }else{
            System.out.println("차량이 반납되지 않았다");
        }
    }

//    public void rent(String carID, String userID) {
//        Car car = carRepository.findByID(carID);
//
//        if (car == null) {
//            System.out.println("차량 정보가 존재하지 않습니다.");
//        } else if (car.isAvailable()) {
//            String rentalID = UUID.randomUUID().toString();
//            double startMileage = car.getCurrentMileage();
//
//            Rental rental = new Rental(rentalID, userID, carID, startMileage);
//            rentalRepository.save(rental);
//
//            car.setAvailable(false);
//            carRepository.save(car);
//
//            System.out.println("차량 [" + carID + "]가 대여되었습니다.");
//        } else {
//            System.out.println("차량 [" + carID + "]는 이미 대여된 차량입니다.");
//        }
//    }

    /** 차량을 가지고 없는지 체크 */
    public void checkHasNoCar() {
        if (sessionManager.getCar() != null) {
            throw new ExitPageException();
        }
    }

//    public void returnCar(String carID, double finalMileage) {
//        Car car = carRepository.findByID(carID);
//
//        if (car == null) {
//            System.out.println("차량 정보가 존재하지 않습니다.");
//            return;
//        }
//
//        if (car.isAvailable()) {
//            System.out.println("차량 [" + carID + "]는 대여 중인 상태가 아니므로 반납할 수 없습니다.");
//            return;
//        }
//
//        Optional<Rental> activeRentalOpt = rentalRepository.findActiveRentalByCarID(carID);
//
//        if (!activeRentalOpt.isPresent()) {
//            System.out.println("활성화된 대여 정보가 없습니다.");
//            return;
//        }
//
//        Rental rental = activeRentalOpt.get();
//        double ratePerKm = 150.0;
//
//        boolean payResult = payService.processReturnPayment(rental, car, finalMileage, ratePerKm);
//
//        if (payResult) {
//            car.setAvailable(true);
//            car.setCurrentMileage(finalMileage);
//            carRepository.save(car);
//
//            rental.setReturnDate(LocalDateTime.now());
//            rental.setEndMileage(finalMileage);
//            rental.setRentalStatus("반납 완료");
//            rentalRepository.save(rental);
//
//            System.out.println("차량 [" + carID + "]가 반납되었습니다.");
//        } else {
//            System.out.println("결제 실패로 반납 처리가 이루어지지 않았습니다.");
//        }
//    }
}
