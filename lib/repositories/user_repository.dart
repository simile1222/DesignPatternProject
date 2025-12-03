import '../models/user.dart';
import '../data/user_dao.dart';

class UserRepository {
  final UserDAO _userDAO = UserDAO();

  Future<User?> findById(String userId) async {
    return await _userDAO.findById(userId);
  }

  Future<bool> existsById(String userId) async {
    return await _userDAO.existsById(userId);
  }

  Future<void> save(User user) async {
    await _userDAO.insert(user);
  }

  Future<void> delete(String userId) async {
    await _userDAO.deleteUser(userId);
  }
}
