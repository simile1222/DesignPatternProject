import 'payment_strategy.dart';
import 'credit_card_payment.dart';
import 'kakao_pay.dart';

class PayStrategyFactory {
  static PayStrategy createPay(String type, {String? cardNumber}) {
    switch (type.toLowerCase()) {
      case 'credit_card':
        if (cardNumber == null) {
          throw Exception('카드번호가 필요합니다');
        }
        return CreditCardPayment(cardNumber: cardNumber);
      case 'kakao_pay':
        return KakaoPay();
      default:
        throw Exception('지원하지 않는 결제 방법입니다');
    }
  }
}
