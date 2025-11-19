package org.example.Payment;

public enum PayStrategyFactory {
    INSTANCE;
    public Pay createPaymentMethod(String methodType) {
        if (methodType == null || methodType.isEmpty()) {
            System.out.println("결제 방식이 선택되지 않았습니다.");
            return null;
        }
        switch (methodType.toUpperCase()) {
            case "KAKAO":
                return new KakaoPay();
            case "CARD":
                return new CreditCardPayment();
            default:
                System.out.println(methodType + "는 지원하지 않는 결제 방식입니다.");
                return null;
        }
    }
}
