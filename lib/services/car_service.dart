import '../models/car.dart';
import '../models/rental.dart';
import '../models/user.dart';
import '../models/search_condition.dart';
import '../repositories/car_repository.dart';
import '../repositories/rental_repository.dart';
import '../exceptions/exit_page_exception.dart';
import 'payment.dart';
import 'price_calculator.dart';
import 'login_service.dart';

class CarService {
  final CarRepository _carRepository = CarRepository();
  final RentalRepository _rentalRepository = RentalRepository();
  final LoginService _loginService = LoginService();
  Payment? _payment;
  Car? _currentCar;

  CarService();

  // 차량을 가지고 있는지 체크
  void checkHasCar() {
    if (_currentCar == null) {
      throw ExitPageException('차량이 없습니다');
    }
  }

  // 차량을 가지고 없는지 체크
  void checkHasNoCar() {
    if (_currentCar != null) {
      throw ExitPageException('이미 차량을 보유하고 있습니다');
    }
  }

  Future<List<Car>> getAvailableCar(SearchCondition condition) async {
    return await _carRepository.findAvailableCars(condition);
  }

  // 면허 인증 포함 대여 기능
  Future<bool> rentCar(String userId, int carId) async {
    // 면허 인증 확인
    final user = await _loginService.getUserById(userId);
    if (user == null || !user.licenseVerified) {
      print('❌ 대여 불가: 면허 미인증 상태입니다.');
      return false;
    }

    // 차량 조회
    final car = await _carRepository.findById(carId);
    if (car == null) {
      print('❌ 차량을 찾을 수 없습니다');
      return false;
    }

    if (car.isRented) {
      print('❌ 이미 대여 중인 차량입니다');
      return false;
    }

    // 차량 상태 업데이트
    await _carRepository.updateStatus(carId, true);
    _currentCar = car.copyWith(isRented: true);

    print('✅ 차량 대여 완료!');
    return true;
  }

  Future<bool> returnCar(int carId) async {
    final car = await _carRepository.findById(carId);
    if (car == null) {
      print('❌ 차량을 찾을 수 없습니다');
      return false;
    }

    // 차량 상태 업데이트
    await _carRepository.updateStatus(carId, false);
    _currentCar = null;

    print('✅ 차량 반납 완료!');
    return true;
  }

  Future<int> calculatePrice(Rental rental, PriceCalculator calculator) async {
    return calculator.calculate(rental);
  }

  void setPaymentMethod(Payment payment) {
    _payment = payment;
  }

  Car? getCurrentCar() {
    return _currentCar;
  }
}
