import '../models/car.dart';
import '../models/search_condition.dart';
import '../data/car_dao.dart';

class CarRepository {
  final CarDAO _carDAO = CarDAO();

  Future<bool> save(Car car) async {
    return await _carDAO.insertCar(car);
  }

  Future<List<Car>?> findAll() async {
    return await _carDAO.getAllCars();
  }

  Future<List<Car>?> findAvailableCars(SearchCondition condition) async {
    return await _carDAO.getAvailableCars(condition);
  }

  Future<Car?> findById(int carId) async {
    return await _carDAO.getCarById(carId);
  }

  Future<bool> updateStatus(int carId, bool isRented) async {
    return await _carDAO.updateRentedStatus(carId, isRented);
  }

  Future<bool> delete(int carId) async {
    return await _carDAO.deleteCar(carId);
  }
}
