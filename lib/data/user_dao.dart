import 'package:sqflite/sqflite.dart';
import '../models/user.dart';
import 'database_manager.dart';

class UserDAO {
  final DatabaseManager _dbManager = DatabaseManager();

  Future<Database> get _db async => await _dbManager.database;

  // 로그인 (SELECT with password)
  Future<User?> logIn(String userId, String hashPassword) async {
    String sql = 'SELECT * FROM users WHERE id = ? AND passwordHash = ?';
    try {
      final db = await _db;
      final List<Map<String, dynamic>> maps = await db.rawQuery(
        sql,
        [userId, hashPassword],
      );

      if (maps.isNotEmpty) {
        return User.fromMap(maps.first);
      }
    } catch (e) {
      print('❌ 로그인 쿼리 실행 실패: $e');
    }

    return null; // 로그인 실패
  }

  // 회원 등록 (CREATE)
  Future<bool> insertUser(User user) async {
    String sql = 'INSERT INTO users (id, passwordHash, licenseVerified, createdAt) VALUES (?, ?, ?, ?)';
    try {
      final db = await _db;
      await db.rawInsert(
        sql,
        [
          user.id,
          user.passwordHash,
          user.licenseVerified ? 1 : 0,
          user.createdAt,
        ],
      );
      print('✅ 회원 등록 완료: ${user.id}');
      return true;
    } catch (e) {
      print('❌ 회원 등록 실패: $e');
      return false;
    }
  }

  // 회원 조회 (READ)
  Future<User?> getUserById(String id) async {
    String sql = 'SELECT * FROM users WHERE id = ?';
    try {
      final db = await _db;
      final List<Map<String, dynamic>> maps = await db.rawQuery(sql, [id]);

      if (maps.isNotEmpty) {
        return User.fromMap(maps.first);
      }
    } catch (e) {
      print('❌ 회원 조회 실패: $e');
    }
    return null;
  }

  // 면허 인증 상태 갱신 (UPDATE)
  Future<bool> updateLicense(String id, bool verified) async {
    String sql = 'UPDATE users SET licenseVerified = ? WHERE id = ?';
    try {
      final db = await _db;
      int result = await db.rawUpdate(sql, [verified ? 1 : 0, id]);
      if (result > 0) {
        print('✅ 면허 상태 변경 완료 ($id → $verified)');
        return true;
      }
    } catch (e) {
      print('❌ 면허 상태 변경 실패: $e');
    }
    return false;
  }

  // 회원 삭제 (DELETE)
  Future<bool> deleteUser(String id) async {
    String sql = 'DELETE FROM users WHERE id = ?';
    try {
      final db = await _db;
      int result = await db.rawDelete(sql, [id]);
      if (result > 0) {
        print('✅ 회원 삭제 완료: $id');
        return true;
      }
    } catch (e) {
      print('❌ 회원 삭제 실패: $e');
    }
    return false;
  }
}
