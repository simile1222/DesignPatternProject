class User {
  final String id;
  final String passwordHash;
  final bool licenseVerified;
  final String createdAt;

  User({
    required this.id,
    required this.passwordHash,
    required this.licenseVerified,
    required this.createdAt,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'passwordHash': passwordHash,
      'licenseVerified': licenseVerified ? 1 : 0,
      'createdAt': createdAt,
    };
  }

  factory User.fromMap(Map<String, dynamic> map) {
    return User(
      id: map['id'] as String,
      passwordHash: map['passwordHash'] as String,
      licenseVerified: (map['licenseVerified'] as int) == 1,
      createdAt: map['createdAt'] as String,
    );
  }

  User copyWith({
    String? id,
    String? passwordHash,
    bool? licenseVerified,
    String? createdAt,
  }) {
    return User(
      id: id ?? this.id,
      passwordHash: passwordHash ?? this.passwordHash,
      licenseVerified: licenseVerified ?? this.licenseVerified,
      createdAt: createdAt ?? this.createdAt,
    );
  }
}
