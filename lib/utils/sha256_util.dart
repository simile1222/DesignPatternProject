import 'dart:convert';
import 'package:crypto/crypto.dart';

class Sha256Util {
  // 인스턴스화 방지
  Sha256Util._();

  static String hash(String input) {
    if (input.isEmpty) {
      throw ArgumentError('input must not be null or empty');
    }

    final bytes = utf8.encode(input);
    final digest = sha256.convert(bytes);
    return digest.toString();
  }

  /// 평문과 기존 해시(16진수 문자열)를 비교하여 일치하면 true 반환.
  /// 해시값은 복호화가 불가능하므로 직접 비교(비교 결과로 일치 여부 판단).
  static bool matches(String input, String expectedHexHash) {
    if (input.isEmpty) {
      throw ArgumentError('input must not be null or empty');
    }
    if (expectedHexHash.isEmpty) {
      throw ArgumentError('expectedHexHash must not be null or empty');
    }

    final computed = hash(input);
    return _constantTimeEquals(computed, expectedHexHash);
  }

  /// 타이밍 공격을 조금 방지하기 위한 상수 시간 비교
  static bool _constantTimeEquals(String a, String b) {
    if (a.length != b.length) return false;

    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a.codeUnitAt(i) ^ b.codeUnitAt(i);
    }
    return result == 0;
  }
}
