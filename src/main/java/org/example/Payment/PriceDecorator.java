package org.example.Payment;

public abstract class PriceDecorator implements PriceCalculator {
    protected PriceCalculator priceCalculator;
    public PriceDecorator(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }
    public int calculatePrice() {
        return priceCalculator.calculatePrice();
    }
}
