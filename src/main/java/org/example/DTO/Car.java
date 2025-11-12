package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {
    private int id;                // 차량 고유 ID (PK)
    private String model;          // 차량 모델명
    private String plateNo;        // 번호판
    private int parkingId;         // 주차장 ID (FK)
    private double pricePerHour;   // 시간당 요금
    private double pricePerKm;     // 거리당 요금
    private int mileage;           // 주행 거리
    private boolean isRented;      // 대여 중 여부
    private String createdAt;      // 등록 시각
}
