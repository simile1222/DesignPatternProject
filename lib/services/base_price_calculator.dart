import '../models/rental.dart';
import 'price_calculator.dart';

class BasePriceCalculator implements PriceCalculator {
  final double basePrice;
  final int hours;

  BasePriceCalculator({
    required this.basePrice,
    required this.hours,
  });

  @override
  int calculate(Rental rental) {
    return (basePrice * hours).toInt();
  }
}
