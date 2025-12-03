package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
public class Rental {
    private String rentalID;
    private String userID;
    private String carID;
    private Integer rentalHour;

    private LocalDateTime rentDate;
    private double startMileage;

    private LocalDateTime returnDate;
    private double endMileage;

    private Integer finalPrice;
    private String rentalStatus;

    public Rental(String rentalID, String userID, String carID, double startMileage) {
        this.rentalID = rentalID;
        this.userID = userID;
        this.carID = carID;
        this.startMileage = startMileage;
        this.rentDate = LocalDateTime.now();
        this.rentalStatus = "대여 중";
    }

}