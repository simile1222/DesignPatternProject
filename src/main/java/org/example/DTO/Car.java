package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Car {
    private String carID;  //차량 아이디
    private String modelName;  //모델명
    private String carPlateNumber;  //번호판
    @Builder.Default
    private boolean isAvailable=false;  //대여 가능 여부

    private int parkingId;         // 주차장 ID (FK)
    private String parkingLocation;  //주차 위치
    @Builder.Default
    private Integer baseRentalPrice=20000;  //기본 대여료
    private double currentMileage;  //현재 주행거리

    private double pricePerHour;   // 시간당 요금
    private double pricePerKm;     // 거리당 요금
    private int mileage;           // 주행 거리
    private String createdAt;      // 등록 시각
    public void printCar(){
        System.out.println(
                "ID: " + getCarID()
                        + " | 모델: " + getModelName()
        );
        System.out.println("--------------------------");
    }

}
