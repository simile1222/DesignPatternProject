import '../models/rental.dart';

abstract class PriceCalculator {
  int calculate(Rental rental);
}
