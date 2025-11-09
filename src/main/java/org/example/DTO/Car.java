package org.example.DTO;

public class Car {
    private String carId;
    private String modelName;
    private String carPlateNumber;
    private Integer price;
    private String parkingLocation;
    private boolean isAvailable;

    public Car(String carID, String modelName, Integer price) {
        this.carID = carID;
        this.modelName = modelName;
        this.price = price;
        this.isAvailable = true;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    public boolean isAvailble() {return isAvailable;}
    public String getCarID() {return carID;}
    public String getModelName() {return modelName;}
    public String getCarPlateNumber() {return carPlateNumber;}
    public Integer getprice() {return price;}
}
