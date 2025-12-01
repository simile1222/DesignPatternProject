package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.Payment.*;

public class PayService {
    private PayStrategyFactory payFactory;
    private Pay currentPayStrategy;

    public PayService() {
        this.payFactory = new PayStrategyFactory();
    }

    public void setPayMethod(String methodType) {
        this.currentPayStrategy = payFactory.createPaymentMethod(methodType);
    }

    public boolean processReturnPayment(Rental rental, Car car, double finalMileage, double ratePerKm, String paymMethod) {
        setPayMethod(paymMethod);
        if (this.currentPayStrategy == null) {
            System.out.println("지원하지 않는 결제 수단입니다.");
            return false;
        }
        double rentalMileage = finalMileage - rental.getStartMileage();
        PriceCalculator priceCalculator = new BasePriceCalculator(car.getBaseRentalPrice());
        priceCalculator = new MileageFeeDecorator(priceCalculator, rentalMileage, ratePerKm);
        int finalPrice = priceCalculator.calculatePrice();
        System.out.println("주행 거리: " + rentalMileage + "km");
        System.out.println("최종 결제 금액: " + finalPrice + "원");

        return this.currentPayStrategy.processPayment(finalPrice);
    }
}