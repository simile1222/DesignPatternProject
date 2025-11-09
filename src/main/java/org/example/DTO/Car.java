package org.example.DTO;

public class Car {
    private String carID;
    private String modelName;
    private String carPlateNumber;
    private Integer baseRentalPrice;
    private String parkingLocation;
    private boolean isAvailable;

    public Car(String carID, String modelName, Integer baseRentalPrice) {
        this.carID = carID;
        this.modelName = modelName;
        this.baseRentalPrice = baseRentalPrice;
        this.isAvailable = true;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    public boolean isAvailble() {return isAvailable;}
    public String getCarID() {return carID;}
    public String getModelName() {return modelName;}
    public String getCarPlateNumber() {return carPlateNumber;}
    public Integer getBaseRentalPrice() {return baseRentalPrice;}
}
