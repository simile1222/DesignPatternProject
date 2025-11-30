package org.example.Payment;

public class CreditCardPayment implements Pay {
    public boolean processPayment(int amount) {
        System.out.println("신용카드로 " + amount + "원이 결제되었습니다.");
        return true;
    }
}
