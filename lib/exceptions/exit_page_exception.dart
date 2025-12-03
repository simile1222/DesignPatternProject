class ExitPageException implements Exception {
  final String? message;

  ExitPageException([this.message]);

  @override
  String toString() {
    if (message == null) return 'ExitPageException';
    return 'ExitPageException: $message';
  }
}
