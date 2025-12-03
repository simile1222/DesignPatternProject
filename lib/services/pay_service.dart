import 'payment_strategy.dart';

class PayService {
  PayStrategy? _payStrategy;

  void setPayStrategy(PayStrategy strategy) {
    _payStrategy = strategy;
  }

  void pay() {
    if (_payStrategy == null) {
      throw Exception('결제 방법이 설정되지 않았습니다');
    }
    // pay() 메서드는 price를 받아야 하지만, Java 코드에서는 인자 없이 호출
    // 실제 사용 시 price를 따로 저장해두고 사용해야 함
  }

  PayStrategy? getPayStrategy() {
    return _payStrategy;
  }
}
