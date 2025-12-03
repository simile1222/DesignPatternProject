import '../models/user.dart';
import '../data/user_dao.dart';
import '../utils/session_manager.dart';
import '../utils/sha256_util.dart';
import '../exceptions/exit_page_exception.dart';

class LoginService {
  final UserDAO _userDAO = UserDAO();
  final SessionManager _sessionManager = SessionManager.instance;

  // 중복된 아이디 확인
  Future<bool> isDuplicated(String userId) async {
    final user = await _userDAO.getUserById(userId);
    return user != null;
  }

  Future<User?> login(String userId, String password) async {
    final hashedPassword = Sha256Util.hash(password);
    final user = await _userDAO.logIn(userId, hashedPassword);

    if (user != null) {
      _sessionManager.setUser(user);
    }

    return user;
  }

  Future<bool> signIn(String userId, String password) async {
    final hashedPassword = Sha256Util.hash(password);
    final user = User(
      id: userId,
      passwordHash: hashedPassword,
      licenseVerified: false,
      createdAt: DateTime.now().toIso8601String(),
    );

    return await _userDAO.insertUser(user);
  }

  Future<bool> updateLicense(String id, bool license) async {
    final success = await _userDAO.updateLicense(id, license);

    // 세션 업데이트
    if (success && _sessionManager.getUser()?.id == id) {
      final updatedUser = await _userDAO.getUserById(id);
      if (updatedUser != null) {
        _sessionManager.setUser(updatedUser);
      }
    }

    return success;
  }

  Future<void> logout() async {
    _sessionManager.clear();
    print('로그아웃 처리');
  }

  // 로그인 되어있는지 확인
  void checkLogIn() {
    if (_sessionManager.getUser() == null) {
      print('로그인이 필요한 서비스');
      throw ExitPageException('로그인이 필요합니다');
    }
  }

  // 로그아웃 되어있는지 확인
  void checkLogOut() {
    if (_sessionManager.getUser() != null) {
      print('로그아웃이 필요한 서비스');
      throw ExitPageException('로그아웃이 필요합니다');
    }
  }

  // 면허 있는지 확인
  void checkLicense() {
    final user = _sessionManager.getUser();
    if (user == null || !user.licenseVerified) {
      print('면허 인증이 필요한 서비스');
      throw ExitPageException('면허 인증이 필요합니다');
    }
  }

  Future<User?> getUserById(String userId) async {
    return await _userDAO.getUserById(userId);
  }

  Future<bool> isLicenseVerified(String userId) async {
    final user = await getUserById(userId);
    return user?.licenseVerified ?? false;
  }
}
