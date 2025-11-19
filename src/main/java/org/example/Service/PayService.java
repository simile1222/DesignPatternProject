package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.Exception.ExitPageException;
import org.example.InputUtil;
import org.example.Payment.*;

public class PayService {

    private PayStrategyFactory payFactory=PayStrategyFactory.INSTANCE;

    private Pay currentPayStrategy;

    public void setPayMethod() {
        int payment = InputUtil.getInt("신용카드","카카오페이");
        String methodType=null;
        switch (payment){
            case 1 ->methodType = "CARD";
            case 2 ->methodType = "KAKAO";
            case 0 -> throw new ExitPageException();
            default -> {
                System.out.println("올바르지 않은 선택지");
                throw new ExitPageException();
            }
        }
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
