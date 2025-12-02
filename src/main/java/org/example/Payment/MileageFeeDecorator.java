package org.example.Payment;

public class MileageFeeDecorator extends PriceDecorator {
    private double mileage;
    private double ratePerKm;
    public MileageFeeDecorator(PriceCalculator priceCalculator, double mileage, double ratePerKm) {
        super(priceCalculator);
        this.mileage = mileage;
        this.ratePerKm = ratePerKm;
    }
    public int calculatePrice() {
        int basePrice = super.calculatePrice();
        int mileageFee = (int) (mileage*ratePerKm);
        System.out.println(" [결제 가격] 기본 대여료: " + basePrice + "원, 주행 거리(" + mileage + "): " + mileageFee);
        return basePrice + mileageFee;
    }
}
