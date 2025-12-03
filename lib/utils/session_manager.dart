import '../models/user.dart';
import '../models/car.dart';

class SessionManager {
  static final SessionManager instance = SessionManager._internal();

  factory SessionManager() {
    return instance;
  }

  SessionManager._internal();

  User? _user;
  Car? _car;

  User? getUser() {
    return _user;
  }

  void setUser(User? user) {
    _user = user;
  }

  Car? getCar() {
    return _car;
  }

  void setCar(Car? car) {
    _car = car;
  }

  void clear() {
    _user = null;
    _car = null;
  }

  bool isLoggedIn() {
    return _user != null;
  }
}
