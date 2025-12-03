import 'package:sqflite/sqflite.dart';
import '../models/car.dart';
import '../models/search_condition.dart';
import 'database_manager.dart';

class CarDAO {
  final DatabaseManager _dbManager = DatabaseManager();

  Future<Database> get _db async => await _dbManager.database;

  // 차량 등록 (INSERT)
  Future<bool> insertCar(Car car) async {
    try {
      final db = await _db;
      await db.insert(
        'cars',
        car.toMap(),
        conflictAlgorithm: ConflictAlgorithm.abort,
      );
      return true;
    } catch (e) {
      print('❌ 차량 등록 실패: $e');
      return false;
    }
  }

  // 전체 차량 조회 (SELECT *)
  Future<List<Car>?> getAllCars() async {
    try {
      final db = await _db;
      final List<Map<String, dynamic>> maps = await db.query('cars');

      return List.generate(maps.length, (i) {
        return Car.fromMap(maps[i]);
      });
    } catch (e) {
      print('❌ 차량 조회 실패: $e');
      return null;
    }
  }

  // 조건 기반 차량 조회 (is_rented = 0만)
  Future<List<Car>?> getAvailableCars(SearchCondition cond) async {
    try {
      final db = await _db;

      // 기본 쿼리 (대여가능한 차량만)
      String whereClause = 'isRented = 0';
      List<dynamic> whereArgs = [];

      // 동적 조건 추가
      if (cond.model.isNotEmpty) {
        whereClause += ' AND model LIKE ?';
        whereArgs.add('%${cond.model}%');
      }

      if (cond.parkingId > 0) {
        whereClause += ' AND parkingId = ?';
        whereArgs.add(cond.parkingId);
      }

      if (cond.minPrice > 0) {
        whereClause += ' AND pricePerHour >= ?';
        whereArgs.add(cond.minPrice);
      }

      if (cond.maxPrice > 0) {
        whereClause += ' AND pricePerHour <= ?';
        whereArgs.add(cond.maxPrice);
      }

      final List<Map<String, dynamic>> maps = await db.query(
        'cars',
        where: whereClause,
        whereArgs: whereArgs,
      );

      if (maps.isEmpty) {
        print('⚠️ 조건에 맞는 대여 가능 차량이 없습니다.');
      }

      return List.generate(maps.length, (i) {
        return Car.fromMap(maps[i]);
      });
    } catch (e) {
      print('❌ 차량 검색 실패: $e');
      return null;
    }
  }

  // 특정 차량 조회 (SELECT by ID)
  Future<Car?> getCarById(int id) async {
    try {
      final db = await _db;
      final List<Map<String, dynamic>> maps = await db.query(
        'cars',
        where: 'id = ?',
        whereArgs: [id],
      );

      if (maps.isEmpty) {
        return null;
      }

      return Car.fromMap(maps.first);
    } catch (e) {
      print('❌ 차량 조회 실패: $e');
      return null;
    }
  }

  // 대여 상태 업데이트 (UPDATE is_rented)
  Future<bool> updateRentedStatus(int carId, bool rented) async {
    try {
      final db = await _db;
      final result = await db.update(
        'cars',
        {'isRented': rented ? 1 : 0},
        where: 'id = ?',
        whereArgs: [carId],
      );
      return result > 0;
    } catch (e) {
      print('❌ 대여 상태 변경 실패: $e');
      return false;
    }
  }

  // 차량 삭제 (DELETE)
  Future<bool> deleteCar(int id) async {
    try {
      final db = await _db;
      final result = await db.delete(
        'cars',
        where: 'id = ?',
        whereArgs: [id],
      );
      return result > 0;
    } catch (e) {
      print('❌ 차량 삭제 실패: $e');
      return false;
    }
  }
}
