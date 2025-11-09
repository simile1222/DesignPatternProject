package org.example.DTO;

public class Car {
    private String carID;
    private String modelName;
    private String carPlateNumber;
    private boolean isAvailable;

    private String parkingLocation;

    private Integer baseRentalPrice;
    private double currentMileage;
    

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
    public void setMileage(double mileage) {
        this.currentMileage = mileage;
    }
    
    public boolean isAvailble() {return isAvailable;}
    public String getCarID() {return carID;}
    public String getModelName() {return modelName;}
    public String getCarPlateNumber() {return carPlateNumber;}
    
    public Integer getBaseRentalPrice() {return baseRentalPrice;}
    public double getCurrentMileage() {return currentMileage;}
}
