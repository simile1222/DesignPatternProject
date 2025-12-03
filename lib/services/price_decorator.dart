import '../models/rental.dart';
import 'price_calculator.dart';

abstract class PriceDecorator implements PriceCalculator {
  final PriceCalculator calculator;

  PriceDecorator(this.calculator);

  @override
  int calculate(Rental rental);
}
