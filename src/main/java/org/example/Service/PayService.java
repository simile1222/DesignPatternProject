package org.example.Service;

<<<<<<< HEAD
import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.Payment.*;
import org.example.Repository.CarRepository;

public class PayService {
    private CarRepository carRepository;
    private PayStrategyFactory payFactory;

    private Pay currentPayStrategy;

    public PayService(CarRepository carRepository) {
        this.carRepository = carRepository;
        this.payFactory = new PayStrategyFactory();
    }

    public void setPayMethod(String methodType) {
        this.currentPayStrategy = payFactory.createPaymentMethod(methodType);
    }

    public boolean processReturnPayment(Rental rental, Car car, double finalMileage, double ratePerKm) {
        double rentalMileage = finalMileage - rental.getStartMileage();
        PriceCalculator priceCalculator = new BasePriceCalculator(car.getBaseRentalPrice());
        priceCalculator = new MileageFeeDecorator(priceCalculator, rentalMileage, ratePerKm);
        int finalPrice = priceCalculator.calculatePrice();
        System.out.println("결제 금액: " + finalPrice + "원");

        boolean payResult = this.currentPayStrategy.processPayment(finalPrice);
        return payResult;
    }
}

/*
public class PayService {
    public void pay(){
        return;
    }
}
 */