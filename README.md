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


## 授權條款
本專案採用 MIT 授權條款。