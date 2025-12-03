import '../models/rental.dart';
import 'price_calculator.dart';
import 'price_decorator.dart';

class MileageFeeDecorator extends PriceDecorator {
  final int mileage;
  final int pricePerKm;

  MileageFeeDecorator(
    PriceCalculator calculator, {
    required this.mileage,
    this.pricePerKm = 100,
  }) : super(calculator);

  @override
  int calculate(Rental rental) {
    int basePrice = calculator.calculate(rental);
    int mileageFee = mileage * pricePerKm;
    return basePrice + mileageFee;
  }
}
