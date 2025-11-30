package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {
    private String carID;  //차량 아이디
    private String modelName;  //모델명
    private String carPlateNumber;  //번호판
    private boolean isAvailable;  //대여 가능 여부

    private int parkingId;         // 주차장 ID (FK)
    private String parkingLocation;  //주차 위치

    private Integer baseRentalPrice;  //기본 대여료
    private double currentMileage;  //현재 주행거리

    private double pricePerHour;   // 시간당 요금
    private double pricePerKm;     // 거리당 요금
    private int mileage;           // 주행 거리
    private String createdAt;      // 등록 시각

    public Car(String carID, String modelName, Integer baseRentalPrice) {
        this.carID = carID;
        this.modelName = modelName;
        this.baseRentalPrice = baseRentalPrice;
        this.isAvailable = true;
        this.currentMileage = 0.0;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    public void setCurrentMileage(double mileage) {
        this.currentMileage = mileage;
    }

    public boolean isAvailable() {return isAvailable;}
    public String getCarID() {return carID;}
    public String getModelName() {return modelName;}
    public String getCarPlateNumber() {return carPlateNumber;}
    
    public Integer getBaseRentalPrice() {return baseRentalPrice;}
    public double getCurrentMileage() {return currentMileage;}
}
