package org.example.Payment;

public class KakaoPay implements Pay {
    public boolean processPayment(int amount) {
        System.out.println("카카오페이로 " + amount + "원이 결제되었습니다.");
        return true;
    }
}