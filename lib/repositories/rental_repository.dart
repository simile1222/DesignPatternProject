import 'package:sqflite/sqflite.dart';
import '../models/rental.dart';
import '../models/user.dart';
import '../models/car.dart';
import '../data/database_manager.dart';

class RentalRepository {
  final DatabaseManager _dbManager = DatabaseManager();

  Future<Database> get _db async => await _dbManager.database;

  Future<List<Rental>> findAll() async {
    final db = await _db;
    final List<Map<String, dynamic>> maps = await db.query('rentals');

    return List.generate(maps.length, (i) {
      return Rental.fromMap(maps[i]);
    });
  }

  Future<Rental?> findById(String rentalId) async {
    final db = await _db;
    final List<Map<String, dynamic>> maps = await db.query(
      'rentals',
      where: 'rentalId = ?',
      whereArgs: [rentalId],
    );

    if (maps.isEmpty) {
      return null;
    }

    return Rental.fromMap(maps.first);
  }

  Future<List<Rental>> findByUser(User user) async {
    final db = await _db;
    final List<Map<String, dynamic>> maps = await db.query(
      'rentals',
      where: 'userId = ?',
      whereArgs: [user.id],
    );

    return List.generate(maps.length, (i) {
      return Rental.fromMap(maps[i]);
    });
  }

  Future<List<Rental>> findByCar(Car car) async {
    final db = await _db;
    final List<Map<String, dynamic>> maps = await db.query(
      'rentals',
      where: 'carId = ?',
      whereArgs: [car.id],
    );

    return List.generate(maps.length, (i) {
      return Rental.fromMap(maps[i]);
    });
  }

  Future<void> save(Rental rental) async {
    final db = await _db;
    await db.insert(
      'rentals',
      rental.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }
}
