package org.example.Repository;

import org.example.DTO.Rental;
import java.util.HashMap;
import java.util.Map;

public class RentalRepository {
    private static RentalRepository instance;
    public static RentalRepository getInstance(){
        if(instance ==null){
            instance = new RentalRepository();
        }
        return instance;
    }
    private Map<String, Rental> rentalDB = new HashMap<>();

    public void save(Rental rental) {
        rentalDB.put(rental.getRentalID(), rental);
    }

    public Rental findByID(String rentalID) {
        return rentalDB.get(rentalID);
    }

    public Rental findActiveRentalByCarID(String carID) {
        for (Rental rental : rentalDB.values()) {
            if (rental.getCarID().equals(carID) && rental.getRentalStatus().equals("대여 중")) {
                return rental;
            }
        }
        return null;
    }
}