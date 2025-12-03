import 'payment_strategy.dart';
import 'payment_strategy_factory.dart';

class Payment {
  PayStrategy? _payStrategy;

  void setPayStrategy(PayStrategy strategy) {
    _payStrategy = strategy;
  }

  void processPayment(int price) {
    if (_payStrategy == null) {
      throw Exception('결제 방법이 설정되지 않았습니다');
    }
    _payStrategy!.pay(price);
  }

  int refund(int amount) {
    // 환불 로직
    print('$amount원 환불 처리');
    return amount;
  }
}
