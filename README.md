# 飯店管理系統

## 專案概述
這是一個使用 Java 實作的簡單飯店管理系統。包含各種組件，如櫃台服務人員、打掃人員、廚師和客人，每個組件都由各自的類別表示。此系統旨在管理飯店營運，包括預約、客房清潔和餐飲服務。

## 專案結構
```
hotel-management-system
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── hotel
│   │   │           ├── Main.java
│   │   │           ├── model
│   │   │           │   ├── Person.java
│   │   │           │   ├── Hotel.java
│   │   │           │   ├── FrontDeskStaff.java
│   │   │           │   ├── Housekeeper.java
│   │   │           │   ├── Chef.java
│   │   │           │   ├── Guest.java
│   │   │           │   └── Room.java
│   │   │           ├── service
│   │   │           │   ├── ReservationService.java
│   │   │           │   ├── HousekeepingService.java
│   │   │           │   └── DiningService.java
│   │   │           └── util
│   │   │               └── DateUtil.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── hotel
│                   └── model
│                       └── HotelTest.java
├── pom.xml
└── README.md
```

## 功能特色
- **人員管理**：飯店所有人員的基礎類別。
- **房間管理**：管理房間細節和可用性。
- **預約系統**：處理客人預約和入住登記。
- **客房清潔服務**：管理清潔排程和任務。
- **餐飲服務**：管理餐廳營運和菜單。

## Code Smell 問題
本專案刻意包含多種 code smell 問題，用於教學目的：
1. **Long Method（過長方法）**：`Person.java` 包含比必要更長的方法。
2. **Duplicate Code（重複程式碼）**：`Hotel.java` 有重複的程式碼片段。
3. **Tight Coupling（緊密耦合）**：`FrontDeskStaff.java` 與其他類別緊密耦合。
4. **Large Class（過大類別）**：`Housekeeper.java` 承擔過多職責。
5. **Data Class（資料類別）**：`Chef.java` 主要只保存資料，缺乏行為。
6. **Excessive Getter/Setter（過多 Getter/Setter）**：`Guest.java` 有過多的 getter 和 setter 方法。
7. **Magic Numbers（魔術數字）**：`Room.java` 包含硬編碼的數值。
8. **Excessive Responsibility（過多職責）**：`ReservationService.java` 處理過多任務。
9. **Unnecessary Complexity（不必要的複雜性）**：`HousekeepingService.java` 有過於複雜的邏輯。
10. **Inconsistent Naming（不一致的命名）**：`DiningService.java` 有命名規則不一致的方法。
11. **Excessive Static Methods（過多靜態方法）**：`DateUtil.java` 使用過多靜態方法。
12. **Long Parameter List（過長參數列表）**：多個類別中的某些方法有過多參數。
13. **Feature Envy（特性依戀）**：某些類別過度依賴其他類別的方法。
14. **Primitive Obsession（基本型別偏執）**：對某些屬性使用基本型別而非小型物件。
15. **Speculative Generality（過度設計）**：某些類別設計了目前不需要的未來功能。

## 開始使用
要執行此專案，請確保已安裝 Java 和 Maven。複製儲存庫並在專案目錄中執行以下命令：

```bash
mvn clean install
```

建置專案後，您可以使用以下命令執行應用程式：

```bash
java -cp target/hotel-management-system-1.0-SNAPSHOT.jar com.hotel.Main
```

或者使用編譯後的 class 檔案：

```bash
javac -encoding UTF-8 -d target/classes -sourcepath src/main/java src/main/java/com/hotel/Main.java src/main/java/com/hotel/model/*.java src/main/java/com/hotel/service/*.java
java -cp target/classes com.hotel.Main
```

## 授權條款
本專案採用 MIT 授權條款。