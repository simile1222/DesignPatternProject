package org.example.DTO;

import lombok.Data;

@Data
public class Car {
    private String carId;
    private String carPlateNumber;
    private Integer price;
    private String parkingLocation;
}
