package org.example.DTO;

import java.time.LocalDateTime;

public class Rental {
    private String rentalID;
    private String userID;
    private String carID;

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

    public String getRentalID() {return rentalID;}
    public String getUserID() {return userID;}
    public String getCarID() {return carID;}
    public double getStartMileage() {return startMileage;}
    public String getRentalStatus() {return rentalStatus;}

    public void setReturnDate(LocalDateTime returnDate) {this.returnDate = returnDate;}
    public void setEndMileage(double endMileage) {this.endMileage = endMileage;}
    public void setFinalPrice(Integer finalPrice) {this.finalPrice = finalPrice;}
    public void setRentalStatus(String rentalStatus) {this.rentalStatus = rentalStatus;}
}