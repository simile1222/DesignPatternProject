import 'payment_strategy.dart';

class CreditCardPayment implements PayStrategy {
  final String cardNumber;

  CreditCardPayment({required this.cardNumber});

  @override
  void pay(int price) {
    // 신용카드 결제 로직
    print('신용카드($cardNumber)로 $price원 결제');
  }
}
