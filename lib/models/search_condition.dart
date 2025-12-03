class SearchCondition {
  final String model;
  final bool isRented;
  final double minPrice;
  final double maxPrice;
  final int parkingId;

  SearchCondition({
    required this.model,
    required this.isRented,
    required this.minPrice,
    required this.maxPrice,
    required this.parkingId,
  });

  SearchCondition copyWith({
    String? model,
    bool? isRented,
    double? minPrice,
    double? maxPrice,
    int? parkingId,
  }) {
    return SearchCondition(
      model: model ?? this.model,
      isRented: isRented ?? this.isRented,
      minPrice: minPrice ?? this.minPrice,
      maxPrice: maxPrice ?? this.maxPrice,
      parkingId: parkingId ?? this.parkingId,
    );
  }
}
