# 차량 렌탈 앱 시퀀스 다이어그램

## 1. 회원가입 시퀀스

```
사용자 -> SignUpScreen: 회원정보 입력 (ID, PW, 면허인증)
SignUpScreen -> LoginService: signIn(userId, password, licenseVerified)
LoginService -> Sha256Util: hash(password)
Sha256Util --> LoginService: hashedPassword
LoginService -> UserDAO: isDuplicated(userId)
UserDAO -> DatabaseManager: database
DatabaseManager --> UserDAO: db
UserDAO -> Database: rawQuery("SELECT * FROM users WHERE id = ?")
Database --> UserDAO: 결과
UserDAO --> LoginService: true/false
alt 중복된 ID
    LoginService --> SignUpScreen: ❌ 이미 존재하는 ID
    SignUpScreen --> 사용자: 오류 메시지 표시
else 사용 가능한 ID
    LoginService -> UserDAO: signIn(user)
    UserDAO -> Database: rawInsert("INSERT INTO users...")
    Database --> UserDAO: 성공
    UserDAO --> LoginService: true
    LoginService --> SignUpScreen: ✅ 회원가입 성공
    SignUpScreen -> LoginScreen: 화면 전환
    LoginScreen --> 사용자: 로그인 화면 표시
end
```

## 2. 로그인 시퀀스

```
사용자 -> LoginScreen: ID/PW 입력 후 로그인 버튼 클릭
LoginScreen -> LoginService: login(userId, password)
LoginService -> Sha256Util: hash(password)
Sha256Util --> LoginService: hashedPassword
LoginService -> UserDAO: logIn(userId, hashedPassword)
UserDAO -> DatabaseManager: database
DatabaseManager --> UserDAO: db
UserDAO -> Database: rawQuery("SELECT * FROM users WHERE id = ? AND passwordHash = ?")
Database --> UserDAO: 결과 Map
alt 로그인 성공
    UserDAO --> LoginService: User 객체
    LoginService -> SessionManager: setUser(user)
    SessionManager --> LoginService: 세션 저장 완료
    LoginService --> LoginScreen: User 객체
    LoginScreen -> HomeScreen: 화면 전환 (isGuest: false)
    HomeScreen --> 사용자: 메인 화면 표시
else 로그인 실패
    UserDAO --> LoginService: null
    LoginService --> LoginScreen: null
    LoginScreen --> 사용자: ❌ 로그인 실패 메시지
end
```

## 3. 게스트 모드 진입 시퀀스

```
사용자 -> WelcomeScreen: "둘러보기" 버튼 클릭
WelcomeScreen -> HomeScreen: 화면 전환 (isGuest: true)
HomeScreen --> 사용자: 메인 화면 표시 (게스트 배너 포함)
사용자 -> HomeScreen: 차량 렌탈 시도
HomeScreen --> 사용자: ⚠️ "로그인이 필요한 서비스입니다" 메시지
HomeScreen -> LoginScreen: 로그인 화면으로 이동
```

## 4. 차량 검색 시퀀스

```
사용자 -> HomeScreen: 검색 조건 입력 (모델명, 주차장, 가격범위)
HomeScreen -> SearchCondition: 검색 조건 객체 생성
HomeScreen -> CarService: getAvailableCar(searchCondition)
CarService -> CarRepository: getAvailableCars(searchCondition)
CarRepository -> CarDAO: getAvailableCars(searchCondition)
CarDAO -> DatabaseManager: database
DatabaseManager --> CarDAO: db
CarDAO -> CarDAO: 동적 쿼리 생성
Note right of CarDAO: whereClause = "isRented = 0"\n+ model LIKE ?\n+ pricePerHour BETWEEN ? AND ?\n+ parkingId = ?
CarDAO -> Database: rawQuery(dynamicSQL, whereArgs)
Database --> CarDAO: List<Map>
CarDAO --> CarRepository: List<Car>?
CarRepository --> CarService: List<Car>?
alt 검색 결과 있음
    CarService --> HomeScreen: List<Car>
    HomeScreen --> 사용자: 차량 목록 표시
else 검색 결과 없음
    CarService --> HomeScreen: 빈 리스트
    HomeScreen --> 사용자: "검색 결과가 없습니다"
end
```

## 5. 차량 렌탈 시퀀스

```
사용자 -> CarDetailScreen: 차량 선택 후 "렌탈하기" 버튼 클릭
CarDetailScreen -> LoginService: checkLogIn()
alt 로그인 안됨
    LoginService -> ExitPageException: throw ExitPageException
    ExitPageException --> CarDetailScreen: 예외 발생
    CarDetailScreen -> LoginScreen: 로그인 화면으로 이동
else 로그인됨
    CarDetailScreen -> SessionManager: getUser()
    SessionManager --> CarDetailScreen: User 객체
    CarDetailScreen -> CarService: rentCar(userId, carId)
    CarService -> LoginService: getUserById(userId)
    LoginService -> UserDAO: getUserById(userId)
    UserDAO -> Database: rawQuery("SELECT * FROM users WHERE id = ?")
    Database --> UserDAO: User Map
    UserDAO --> LoginService: User 객체
    LoginService --> CarService: User 객체
    alt 면허 미인증
        CarService --> CarDetailScreen: false
        CarDetailScreen --> 사용자: ❌ "면허 미인증 상태"
    else 면허 인증됨
        CarService -> CarRepository: getCarById(carId)
        CarRepository -> CarDAO: getCarById(carId)
        CarDAO -> Database: rawQuery("SELECT * FROM cars WHERE id = ?")
        Database --> CarDAO: Car Map
        CarDAO --> CarRepository: Car?
        CarRepository --> CarService: Car?
        alt 차량 이미 렌탈됨
            CarService --> CarDetailScreen: false
            CarDetailScreen --> 사용자: ❌ "이미 렌탈된 차량"
        else 렌탈 가능
            CarService -> SessionManager: setCar(car)
            SessionManager --> CarService: 세션 저장 완료
            CarService -> RentalRepository: createRental(rental)
            RentalRepository -> Database: rawInsert("INSERT INTO rentals...")
            Database --> RentalRepository: 성공
            RentalRepository --> CarService: true
            CarService -> CarRepository: updateCarRentStatus(carId, true)
            CarRepository -> CarDAO: updateCarRentStatus(carId, true)
            CarDAO -> Database: rawUpdate("UPDATE cars SET isRented = 1 WHERE id = ?")
            Database --> CarDAO: 성공
            CarDAO --> CarRepository: true
            CarRepository --> CarService: true
            CarService --> CarDetailScreen: true
            CarDetailScreen -> PaymentScreen: 결제 화면으로 이동
        end
    end
end
```

## 6. 가격 계산 시퀀스 (Decorator Pattern)

```
사용자 -> PaymentScreen: 렌탈 정보 확인
PaymentScreen -> SessionManager: getCar()
SessionManager --> PaymentScreen: Car 객체
PaymentScreen -> BasePriceCalculator: new BasePriceCalculator(car)
BasePriceCalculator --> PaymentScreen: calculator
PaymentScreen -> MileageFeeDecorator: new MileageFeeDecorator(calculator, mileage)
MileageFeeDecorator --> PaymentScreen: decoratedCalculator
PaymentScreen -> TimeFeeDecorator: new TimeFeeDecorator(decoratedCalculator, hours)
TimeFeeDecorator --> PaymentScreen: finalCalculator
PaymentScreen -> finalCalculator: calculatePrice()
finalCalculator -> TimeFeeDecorator: calculatePrice()
TimeFeeDecorator -> MileageFeeDecorator: calculatePrice()
MileageFeeDecorator -> BasePriceCalculator: calculatePrice()
BasePriceCalculator --> MileageFeeDecorator: basePrice
Note right of MileageFeeDecorator: basePrice + (mileage × pricePerKm)
MileageFeeDecorator --> TimeFeeDecorator: price + mileageFee
Note right of TimeFeeDecorator: price + (hours × pricePerHour)
TimeFeeDecorator --> PaymentScreen: totalPrice
PaymentScreen --> 사용자: 총 금액 표시
```

## 7. 결제 시퀀스 (Strategy Pattern + Factory Pattern)

```
사용자 -> PaymentScreen: 결제 수단 선택 (신용카드/카카오페이)
PaymentScreen -> PayStrategyFactory: getPayStrategy(paymentType)
alt 신용카드 선택
    PayStrategyFactory -> CreditCardPayment: new CreditCardPayment()
    CreditCardPayment --> PayStrategyFactory: strategy
else 카카오페이 선택
    PayStrategyFactory -> KakaoPay: new KakaoPay()
    KakaoPay --> PayStrategyFactory: strategy
end
PayStrategyFactory --> PaymentScreen: PaymentStrategy 객체
사용자 -> PaymentScreen: "결제하기" 버튼 클릭
PaymentScreen -> PayService: processPayment(strategy, price)
PayService -> PaymentStrategy: pay(price)
alt 신용카드
    PaymentStrategy --> PayService: "✅ 신용카드 결제 완료"
else 카카오페이
    PaymentStrategy --> PayService: "✅ 카카오페이 결제 완료"
end
PayService --> PaymentScreen: 결제 완료
PaymentScreen --> 사용자: ✅ "결제가 완료되었습니다"
PaymentScreen -> HomeScreen: 메인 화면으로 이동
```

## 8. 차량 반납 시퀀스

```
사용자 -> RentalHistoryScreen: "반납하기" 버튼 클릭
RentalHistoryScreen -> LoginService: checkLogIn()
LoginService -> SessionManager: getUser()
alt 로그인 안됨
    SessionManager --> LoginService: null
    LoginService -> ExitPageException: throw
    ExitPageException --> RentalHistoryScreen: 예외
    RentalHistoryScreen -> LoginScreen: 로그인 화면으로 이동
else 로그인됨
    SessionManager --> LoginService: User 객체
    RentalHistoryScreen -> CarService: returnCar(rentalId)
    CarService -> RentalRepository: getRentalById(rentalId)
    RentalRepository -> Database: rawQuery("SELECT * FROM rentals WHERE id = ?")
    Database --> RentalRepository: Rental Map
    RentalRepository --> CarService: Rental?
    alt 렌탈 정보 없음
        CarService --> RentalHistoryScreen: false
        RentalHistoryScreen --> 사용자: ❌ "렌탈 정보를 찾을 수 없습니다"
    else 렌탈 정보 있음
        CarService -> RentalRepository: updateRentalEndDate(rentalId, endDate)
        RentalRepository -> Database: rawUpdate("UPDATE rentals SET endDate = ? WHERE id = ?")
        Database --> RentalRepository: 성공
        RentalRepository --> CarService: true
        CarService -> CarRepository: updateCarRentStatus(carId, false)
        CarRepository -> CarDAO: updateCarRentStatus(carId, false)
        CarDAO -> Database: rawUpdate("UPDATE cars SET isRented = 0 WHERE id = ?")
        Database --> CarDAO: 성공
        CarDAO --> CarRepository: true
        CarRepository --> CarService: true
        CarService -> SessionManager: setCar(null)
        SessionManager --> CarService: 세션 클리어 완료
        CarService --> RentalHistoryScreen: true
        RentalHistoryScreen --> 사용자: ✅ "반납이 완료되었습니다"
        RentalHistoryScreen -> HomeScreen: 메인 화면으로 이동
    end
end
```

## 9. 로그아웃 시퀀스

```
사용자 -> ProfileScreen: "로그아웃" 버튼 클릭
ProfileScreen -> LoginService: logout()
LoginService -> SessionManager: clear()
SessionManager -> SessionManager: _user = null
SessionManager -> SessionManager: _car = null
SessionManager --> LoginService: 세션 클리어 완료
LoginService --> ProfileScreen: 로그아웃 완료
ProfileScreen -> WelcomeScreen: 웰컴 화면으로 이동
WelcomeScreen --> 사용자: 초기 화면 표시
```

## 10. 면허 인증 시퀀스

```
사용자 -> ProfileScreen: "면허 인증하기" 버튼 클릭
ProfileScreen -> LoginService: checkLogIn()
alt 로그인 안됨
    LoginService -> ExitPageException: throw
    ExitPageException --> ProfileScreen: 예외
    ProfileScreen -> LoginScreen: 로그인 화면으로 이동
else 로그인됨
    ProfileScreen -> LicenseVerificationScreen: 면허 인증 화면으로 이동
    사용자 -> LicenseVerificationScreen: 면허증 사진 업로드 및 정보 입력
    LicenseVerificationScreen -> LoginService: verifyLicense(userId)
    LoginService -> UserDAO: updateLicenseVerified(userId, true)
    UserDAO -> Database: rawUpdate("UPDATE users SET licenseVerified = 1 WHERE id = ?")
    Database --> UserDAO: 성공
    UserDAO --> LoginService: true
    LoginService -> UserDAO: getUserById(userId)
    UserDAO -> Database: rawQuery("SELECT * FROM users WHERE id = ?")
    Database --> UserDAO: User Map
    UserDAO --> LoginService: User 객체
    LoginService -> SessionManager: setUser(updatedUser)
    SessionManager --> LoginService: 세션 업데이트 완료
    LoginService --> LicenseVerificationScreen: true
    LicenseVerificationScreen --> 사용자: ✅ "면허 인증이 완료되었습니다"
    LicenseVerificationScreen -> ProfileScreen: 프로필 화면으로 이동
end
```

---

## 아키텍처 레이어 구조

```
[Presentation Layer]
    ↓
[Service Layer]
    ↓
[Repository Layer]
    ↓
[DAO Layer]
    ↓
[Database Layer]

횡단 관심사:
- SessionManager (세션 관리)
- Sha256Util (암호화)
- ExitPageException (예외 처리)
```

## 디자인 패턴 적용

1. **Singleton Pattern**: DatabaseManager, SessionManager
2. **DAO Pattern**: UserDAO, CarDAO (데이터 접근 추상화)
3. **Repository Pattern**: 비즈니스 로직과 데이터 접근 분리
4. **Decorator Pattern**: PriceCalculator (동적 가격 계산)
5. **Strategy Pattern**: PaymentStrategy (결제 수단 전략)
6. **Factory Pattern**: PayStrategyFactory (결제 전략 생성)
