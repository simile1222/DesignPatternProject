package org.example.Service;

import org.example.DTO.Car;
import org.example.DTO.Rental;
import org.example.Payment.*;
import org.example.SessionManager;

public class PayService {
    private static PayService instance;
    public static PayService getInstance(){
        if(instance==null){
            instance=new PayService();
        }
        return instance;
    }
    private PayStrategyFactory payFactory;
    private Pay currentPayStrategy;

    public PayService() {
        this.payFactory = new PayStrategyFactory();
    }

    public void setPayMethod(String methodType) {
        this.currentPayStrategy = payFactory.createPaymentMethod(methodType);
    }

    public boolean processReturnPayment(String payMethod) {
        setPayMethod(payMethod);
        if (this.currentPayStrategy == null) {
            System.out.println("지원하지 않는 결제 수단입니다.");
            return false;
        }
        Rental rental = SessionManager.INSTANCE.getRental();
        Car car = SessionManager.INSTANCE.getCar();
        double ratePerHour = car.getPricePerHour();
        PriceCalculator priceCalculator = new BasePriceCalculator(car.getBaseRentalPrice());
        priceCalculator = new MileageFeeDecorator(priceCalculator, rental.getRentalHour(), ratePerHour);
        int finalPrice = priceCalculator.calculatePrice();
        System.out.println("주행 시간: " + rental.getRentalHour());
        System.out.println("최종 결제 금액: " + finalPrice + "원");

        return this.currentPayStrategy.processPayment(finalPrice);
    }
}