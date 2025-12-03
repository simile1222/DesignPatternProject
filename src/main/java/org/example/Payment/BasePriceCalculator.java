package org.example.Payment;

public class BasePriceCalculator implements PriceCalculator {
    private int basePrice;

    public BasePriceCalculator(int basePrice) {
        this.basePrice = basePrice;
    }
    public int calculatePrice() {
        return this.basePrice;
    }
}