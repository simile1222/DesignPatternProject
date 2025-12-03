class Car {
  final int id;
  final String model;
  final String plateNo;
  final int parkingId;
  final double pricePerHour;
  final double pricePerKm;
  final int mileage;
  final bool isRented;
  final String createdAt;

  Car({
    required this.id,
    required this.model,
    required this.plateNo,
    required this.parkingId,
    required this.pricePerHour,
    required this.pricePerKm,
    required this.mileage,
    required this.isRented,
    required this.createdAt,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'model': model,
      'plateNo': plateNo,
      'parkingId': parkingId,
      'pricePerHour': pricePerHour,
      'pricePerKm': pricePerKm,
      'mileage': mileage,
      'isRented': isRented ? 1 : 0,
      'createdAt': createdAt,
    };
  }

  factory Car.fromMap(Map<String, dynamic> map) {
    return Car(
      id: map['id'] as int,
      model: map['model'] as String,
      plateNo: map['plateNo'] as String,
      parkingId: map['parkingId'] as int,
      pricePerHour: (map['pricePerHour'] as num).toDouble(),
      pricePerKm: (map['pricePerKm'] as num).toDouble(),
      mileage: map['mileage'] as int,
      isRented: (map['isRented'] as int) == 1,
      createdAt: map['createdAt'] as String,
    );
  }

  Car copyWith({
    int? id,
    String? model,
    String? plateNo,
    int? parkingId,
    double? pricePerHour,
    double? pricePerKm,
    int? mileage,
    bool? isRented,
    String? createdAt,
  }) {
    return Car(
      id: id ?? this.id,
      model: model ?? this.model,
      plateNo: plateNo ?? this.plateNo,
      parkingId: parkingId ?? this.parkingId,
      pricePerHour: pricePerHour ?? this.pricePerHour,
      pricePerKm: pricePerKm ?? this.pricePerKm,
      mileage: mileage ?? this.mileage,
      isRented: isRented ?? this.isRented,
      createdAt: createdAt ?? this.createdAt,
    );
  }
}
