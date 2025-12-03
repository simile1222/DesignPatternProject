import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class DatabaseManager {
  static final DatabaseManager _instance = DatabaseManager._internal();
  static Database? _database;

  factory DatabaseManager() {
    return _instance;
  }

  DatabaseManager._internal();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDatabase();
    return _database!;
  }

  Future<Database> _initDatabase() async {
    String path = join(await getDatabasesPath(), 'car_rental.db');

    return await openDatabase(
      path,
      version: 1,
      onCreate: _onCreate,
    );
  }

  Future<void> _onCreate(Database db, int version) async {
    await db.execute('''
      CREATE TABLE users(
        id TEXT PRIMARY KEY,
        passwordHash TEXT NOT NULL,
        licenseVerified INTEGER NOT NULL DEFAULT 0,
        createdAt TEXT NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE cars(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        model TEXT NOT NULL,
        plateNo TEXT NOT NULL UNIQUE,
        parkingId INTEGER NOT NULL,
        pricePerHour REAL NOT NULL,
        pricePerKm REAL NOT NULL,
        mileage INTEGER NOT NULL DEFAULT 0,
        isRented INTEGER NOT NULL DEFAULT 0,
        createdAt TEXT NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE rentals(
        rentalId TEXT PRIMARY KEY,
        userId TEXT NOT NULL,
        carId INTEGER NOT NULL,
        startTime TEXT NOT NULL,
        endTime TEXT NOT NULL,
        distance INTEGER NOT NULL,
        FOREIGN KEY (userId) REFERENCES users (id),
        FOREIGN KEY (carId) REFERENCES cars (id)
      )
    ''');

    // 샘플 데이터 삽입
    await _insertSampleData(db);
  }

  Future<void> _insertSampleData(Database db) async {
    final now = DateTime.now().toIso8601String();

    // 샘플 차량 데이터
    await db.insert('cars', {
      'model': 'BMW 5 Series',
      'plateNo': '12가3456',
      'parkingId': 1,
      'pricePerHour': 15000.0,
      'pricePerKm': 200.0,
      'mileage': 0,
      'isRented': 0,
      'createdAt': now,
    });

    await db.insert('cars', {
      'model': 'Mercedes-Benz E-Class',
      'plateNo': '34나5678',
      'parkingId': 1,
      'pricePerHour': 18000.0,
      'pricePerKm': 250.0,
      'mileage': 0,
      'isRented': 0,
      'createdAt': now,
    });

    await db.insert('cars', {
      'model': 'Audi A6',
      'plateNo': '56다7890',
      'parkingId': 2,
      'pricePerHour': 16000.0,
      'pricePerKm': 220.0,
      'mileage': 0,
      'isRented': 0,
      'createdAt': now,
    });

    await db.insert('cars', {
      'model': 'Hyundai Sonata',
      'plateNo': '78라1234',
      'parkingId': 2,
      'pricePerHour': 8000.0,
      'pricePerKm': 150.0,
      'mileage': 0,
      'isRented': 0,
      'createdAt': now,
    });

    await db.insert('cars', {
      'model': 'Kia K5',
      'plateNo': '90마5678',
      'parkingId': 1,
      'pricePerHour': 7500.0,
      'pricePerKm': 150.0,
      'mileage': 0,
      'isRented': 0,
      'createdAt': now,
    });
  }

  Future<void> close() async {
    final db = await database;
    await db.close();
  }
}
