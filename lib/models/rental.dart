class Rental {
  final String rentalId;
  final String userId;
  final int carId;
  final DateTime startTime;
  final DateTime endTime;
  final int distance;

  Rental({
    required this.rentalId,
    required this.userId,
    required this.carId,
    required this.startTime,
    required this.endTime,
    required this.distance,
  });

  Map<String, dynamic> toMap() {
    return {
      'rentalId': rentalId,
      'userId': userId,
      'carId': carId,
      'startTime': startTime.toIso8601String(),
      'endTime': endTime.toIso8601String(),
      'distance': distance,
    };
  }

  factory Rental.fromMap(Map<String, dynamic> map) {
    return Rental(
      rentalId: map['rentalId'] as String,
      userId: map['userId'] as String,
      carId: map['carId'] as int,
      startTime: DateTime.parse(map['startTime'] as String),
      endTime: DateTime.parse(map['endTime'] as String),
      distance: map['distance'] as int,
    );
  }

  Rental copyWith({
    String? rentalId,
    String? userId,
    int? carId,
    DateTime? startTime,
    DateTime? endTime,
    int? distance,
  }) {
    return Rental(
      rentalId: rentalId ?? this.rentalId,
      userId: userId ?? this.userId,
      carId: carId ?? this.carId,
      startTime: startTime ?? this.startTime,
      endTime: endTime ?? this.endTime,
      distance: distance ?? this.distance,
    );
  }
}
