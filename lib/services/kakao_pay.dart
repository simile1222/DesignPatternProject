import 'payment_strategy.dart';

class KakaoPay implements PayStrategy {
  @override
  void pay(int price) {
    // 카카오페이 결제 로직
    print('카카오페이로 $price원 결제');
  }
}
