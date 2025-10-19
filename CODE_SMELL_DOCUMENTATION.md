
# 飯店管理系統 - Code Smell 詳細文件
---

## Code Smell 清單與詳細說明

### 1. Long Class（過長類別）

**位置**: `FrontDeskStaff.java` (第 7 行開始)

**問題描述**: 
類別承擔過多職責，違反單一職責原則（Single Responsibility Principle）。FrontDeskStaff 類別包含了入住、退房、列印客人資訊、計算薪資等多種不同的職責。

**程式碼範例**:
```java
public class FrontDeskStaff extends Person {
    // 包含太多不同職責的方法
    public void checkIn(Guest guest, String roomNumber, int nights) { ... }
    public void checkOut(Guest guest) { ... }
    public void printGuestDetails(Guest guest) { ... }
    public double calculateSalary(String position) { ... }
}
```

**為何有問題**:
- 違反單一職責原則（SRP），一個類別應該只有一個改變的理由
- 類別過於複雜，難以維護和測試
- 當需要修改某一功能時，可能影響到其他不相關的功能

**改善建議**:
- 將薪資計算功能提取到 `SalaryCalculator` 類別
- 將入住/退房功能提取到 `CheckInService` 和 `CheckOutService`
- 將客人資訊列印移到 `Guest` 類別本身

---

### 2. Magic Number（魔術數字）

**位置**: `FrontDeskStaff.java` (第 12-13 行, 第 45 行, 第 72 行, 第 98-106 行)

**問題描述**: 
直接在程式碼中使用數字字面值，沒有給予明確的語義意義，使程式碼難以理解和維護。

**程式碼範例**:
```java
private double baseSalary = 30000;  // 這是什麼標準？
private int workHours = 8;           // 為什麼是 8？
double roomRate = 2000;              // 房價寫死在程式碼中
long days = diff / (24 * 60 * 60 * 1000);  // 魔術數字進行時間計算
```

**為何有問題**:
- 數字的意義不明確，需要閱讀註解或上下文才能理解
- 當需要修改這些值時，必須在所有出現的地方都修改
- 容易出現錯誤，例如在某處忘記修改

**改善建議**:
```java
// 定義具名常數
private static final double STANDARD_BASE_SALARY = 30000.0;
private static final int STANDARD_WORK_HOURS = 8;
private static final double STANDARD_ROOM_RATE = 2000.0;
private static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

// 薪資等級常數
private static final double JUNIOR_SALARY = 30000.0;
private static final double SENIOR_SALARY = 45000.0;
private static final double MANAGER_SALARY = 60000.0;
private static final double DIRECTOR_SALARY = 80000.0;
```

---

### 3. Long Method（過長方法）

**位置**: `FrontDeskStaff.java` - `checkIn()` 方法 (第 23-54 行)

**問題描述**: 
方法包含過多邏輯（32 行程式碼），同時處理驗證、資料設定、日期計算、費用計算和輸出，應該拆分為更小的方法。

**程式碼範例**:
```java
public void checkIn(Guest guest, String roomNumber, int nights) {
    // 1. 輸出資訊
    System.out.println("=== Begin check-in ===");
    
    // 2. 驗證
    if (guest.getName() == null || guest.getName().isEmpty()) { ... }
    
    // 3. 設定房間
    guest.setRoomNumber(roomNumber);
    
    // 4. 計算日期
    Date now = new Date();
    Date checkOut = new Date(now.getTime() + nights * 24 * 60 * 60 * 1000);
    
    // 5. 計算費用
    double roomRate = 2000;
    double totalCost = roomRate * nights;
    
    // 6. 列印收據
    System.out.println("Nights: " + nights);
    ...
}
```

**為何有問題**:
- 一個方法做太多事情，違反單一職責原則
- 難以測試，無法單獨測試驗證、計算等子功能
- 難以閱讀和理解
- 程式碼重用性低

**改善建議**:
```java
public void checkIn(Guest guest, String roomNumber, int nights) {
    validateGuest(guest);
    assignRoom(guest, roomNumber);
    setCheckInDates(guest, nights);
    double cost = calculateCost(nights);
    printCheckInReceipt(guest, roomNumber, nights, cost);
}

private void validateGuest(Guest guest) { ... }
private void assignRoom(Guest guest, String roomNumber) { ... }
private void setCheckInDates(Guest guest, int nights) { ... }
private double calculateCost(int nights) { ... }
private void printCheckInReceipt(...) { ... }
```

---

### 4. Duplicate Code（重複程式碼）

**位置**: `FrontDeskStaff.java` - `checkIn()` 和 `checkOut()` 方法

**問題描述**: 
`checkIn()` 和 `checkOut()` 方法中有大量重複的驗證邏輯和費用計算邏輯。

**程式碼範例**:
```java
// checkIn() 方法中
if (guest.getName() == null || guest.getName().isEmpty()) {
    System.out.println("Error: Guest name cannot be empty");
    return;
}
double roomRate = 2000;
double totalCost = roomRate * nights;

// checkOut() 方法中 - 完全相同的驗證
if (guest.getName() == null || guest.getName().isEmpty()) {
    System.out.println("Error: Guest name cannot be empty");
    return;
}
double roomRate = 2000;  // 重複的房價
long days = diff / (24 * 60 * 60 * 1000);  // 重複的時間計算
```

**為何有問題**:
- 增加維護成本，修改一處可能需要修改多處
- 容易造成不一致，例如只修改了其中一處
- 增加程式碼量，降低可讀性
- 違反 DRY 原則（Don't Repeat Yourself）

**改善建議**:
```java
// 提取共用方法
private void validateGuestName(Guest guest) {
    if (guest.getName() == null || guest.getName().isEmpty()) {
        throw new IllegalArgumentException("Guest name cannot be empty");
    }
}

private double getRoomRate() {
    return STANDARD_ROOM_RATE;
}

private long calculateDays(Date start, Date end) {
    long diff = end.getTime() - start.getTime();
    return diff / MILLISECONDS_PER_DAY;
}

private double calculateTotalCost(long days) {
    return getRoomRate() * days;
}
```

---

### 5. Feature Envy（特性依戀）

**位置**: `FrontDeskStaff.java` - `printGuestDetails()` 方法 (第 84-90 行)

**問題描述**: 
方法過度使用另一個物件（Guest）的資料，這個方法應該屬於 `Guest` 類別本身，而不是 `FrontDeskStaff`。

**程式碼範例**:
```java
// 在 FrontDeskStaff 類別中
public void printGuestDetails(Guest guest) {
    System.out.println("Name: " + guest.getName());
    System.out.println("Age: " + guest.getAge());
    System.out.println("Contact: " + guest.getContactInfo());
    System.out.println("Room number: " + guest.getRoomNumber());
    System.out.println("Check-in date: " + guest.getCheckInDate());
}
```

**為何有問題**:
- 方法過度依賴另一個類別的資料
- 違反物件導向的封裝原則
- 當 Guest 類別的內部結構改變時，這個方法也需要修改
- 資料和操作資料的方法應該在同一個類別中

**改善建議**:
```java
// 將方法移到 Guest 類別中
public class Guest extends Person {
    // ... 其他欄位和方法
    
    public void printDetails() {
        System.out.println("Name: " + getName());
        System.out.println("Age: " + getAge());
        System.out.println("Contact: " + getContactInfo());
        System.out.println("Room number: " + roomNumber);
        System.out.println("Check-in date: " + checkInDate);
    }
}

// 在 FrontDeskStaff 中只需呼叫
public void displayGuestInfo(Guest guest) {
    guest.printDetails();
}
```

---

### 6. Switch Statement（複雜的 Switch 語句）

**位置**: `FrontDeskStaff.java` - `calculateSalary()` 方法 (第 94-112 行)

**問題描述**: 
使用 switch 語句根據職位計算薪資，這種做法缺乏彈性，應考慮使用多型或策略模式。

**程式碼範例**:
```java
public double calculateSalary(String position) {
    double salary = 0;
    switch (position) {
        case "junior":
            salary = 30000;
            break;
        case "senior":
            salary = 45000;
            break;
        case "manager":
            salary = 60000;
            break;
        case "director":
            salary = 80000;
            break;
        default:
            salary = 25000;
    }
    return salary;
}
```

**為何有問題**:
- 每次新增職位時都需要修改這個方法
- 違反開放封閉原則（Open-Closed Principle）
- 容易忘記處理某些情況
- 薪資計算邏輯可能比這更複雜，switch 無法處理

**改善建議**:

**方案 1：使用策略模式**
```java
// 定義薪資計算策略介面
interface SalaryStrategy {
    double calculate();
}

class JuniorSalaryStrategy implements SalaryStrategy {
    public double calculate() { return 30000; }
}

class SeniorSalaryStrategy implements SalaryStrategy {
    public double calculate() { return 45000; }
}

// 使用
public class FrontDeskStaff {
    private SalaryStrategy salaryStrategy;
    
    public double calculateSalary() {
        return salaryStrategy.calculate();
    }
}
```

**方案 2：使用多型**
```java
abstract class Employee {
    abstract double calculateSalary();
}

class JuniorStaff extends Employee {
    double calculateSalary() { return 30000; }
}

class SeniorStaff extends Employee {
    double calculateSalary() { return 45000; }
}
```

---

### 6. Divergent Change（發散式變化）

**位置**: `FrontDeskStaff.java` (第 17-22 行欄位, 第 97-136 行方法)

**問題描述**: 
一個類別因為多種不同的原因而需要修改。FrontDeskStaff 類別會因為薪資政策變更、定價政策變更、折扣政策變更等不同原因而需要修改，導致類別職責不清。

**程式碼範例**:
```java
public class FrontDeskStaff extends Person {
    // 這些欄位會因為不同的業務原因而改變
    private double seniorStaffBonus = 15000;    // 當獎金政策改變時需修改
    private double managerBonus = 30000;        // 當獎金政策改變時需修改
    private double directorBonus = 50000;       // 當獎金政策改變時需修改
    private String peakSeasonSurcharge = "20%"; // 當定價政策改變時需修改
    private String loyaltyDiscountRate = "10%"; // 當折扣政策改變時需修改
    
    // 方法 1：當薪資計算政策改變時需修改
    public double calculateSalaryWithBonus(String position) {
        double salary = baseSalary;
        if (position.equals("senior")) {
            salary += seniorStaffBonus;
        } else if (position.equals("manager")) {
            salary += managerBonus;
        } else if (position.equals("director")) {
            salary += directorBonus;
        }
        return salary;
    }
    
    // 方法 2：當房價定價政策改變時需修改
    public double calculateRoomPrice(int nights, boolean isPeakSeason) {
        double basePrice = 2000 * nights;
        if (isPeakSeason) {
            basePrice *= 1.2; // 20% surcharge
        }
        return basePrice;
    }
    
    // 方法 3：當折扣政策改變時需修改
    public double applyLoyaltyDiscount(double amount, boolean isLoyalMember) {
        if (isLoyalMember) {
            return amount * 0.9; // 10% discount
        }
        return amount;
    }
}
```

**為何有問題**:
- 違反單一職責原則（Single Responsibility Principle）
- 一個類別因為多種不同原因而需要修改
- 當業務規則改變時，需要修改同一個類別的不同部分
- 增加類別的複雜度和維護成本
- 難以進行單元測試
- 不同的修改原因可能互相干擾

**Divergent Change vs. Shotgun Surgery**:
- **Divergent Change（發散式變化）**：一種變更需要修改**一個類別**的多個方法
- **Shotgun Surgery（散彈式修改）**：一種變更需要修改**多個類別**的多個方法

**改善建議**:

**方案 1：將不同職責拆分到不同的類別**
```java
// 1. 薪資計算獨立出來
public class SalaryCalculator {
    private static final double BASE_SALARY = 30000;
    private static final double SENIOR_BONUS = 15000;
    private static final double MANAGER_BONUS = 30000;
    private static final double DIRECTOR_BONUS = 50000;
    
    public double calculateSalary(String position) {
        switch (position) {
            case "senior":
                return BASE_SALARY + SENIOR_BONUS;
            case "manager":
                return BASE_SALARY + MANAGER_BONUS;
            case "director":
                return BASE_SALARY + DIRECTOR_BONUS;
            default:
                return BASE_SALARY;
        }
    }
}

// 2. 定價政策獨立出來
public class PricingPolicy {
    private static final double BASE_ROOM_RATE = 2000;
    private static final double PEAK_SEASON_MULTIPLIER = 1.2;
    
    public double calculateRoomPrice(int nights, boolean isPeakSeason) {
        double basePrice = BASE_ROOM_RATE * nights;
        if (isPeakSeason) {
            basePrice *= PEAK_SEASON_MULTIPLIER;
        }
        return basePrice;
    }
}

// 3. 折扣政策獨立出來
public class DiscountPolicy {
    private static final double LOYALTY_DISCOUNT_RATE = 0.1;
    
    public double applyLoyaltyDiscount(double amount, boolean isLoyalMember) {
        if (isLoyalMember) {
            return amount * (1 - LOYALTY_DISCOUNT_RATE);
        }
        return amount;
    }
}

// 4. 簡化後的 FrontDeskStaff
public class FrontDeskStaff extends Person {
    private String shift;
    private String[] responsibilities;
    private SalaryCalculator salaryCalculator;
    private PricingPolicy pricingPolicy;
    private DiscountPolicy discountPolicy;
    
    public FrontDeskStaff(String name, int age, String contactInfo, 
                         String shift, String[] responsibilities) {
        super(name, age, contactInfo);
        this.shift = shift;
        this.responsibilities = responsibilities;
        this.salaryCalculator = new SalaryCalculator();
        this.pricingPolicy = new PricingPolicy();
        this.discountPolicy = new DiscountPolicy();
    }
    
    // 委派給對應的政策類別
    public double calculateSalary(String position) {
        return salaryCalculator.calculateSalary(position);
    }
    
    public double calculateRoomPrice(int nights, boolean isPeakSeason) {
        return pricingPolicy.calculateRoomPrice(nights, isPeakSeason);
    }
    
    public double applyDiscount(double amount, boolean isLoyalMember) {
        return discountPolicy.applyLoyaltyDiscount(amount, isLoyalMember);
    }
}
```

**方案 2：使用策略模式**
```java
// 定義策略介面
interface PricingStrategy {
    double calculatePrice(int nights);
}

class StandardPricing implements PricingStrategy {
    public double calculatePrice(int nights) {
        return 2000 * nights;
    }
}

class PeakSeasonPricing implements PricingStrategy {
    public double calculatePrice(int nights) {
        return 2000 * nights * 1.2;
    }
}

// FrontDeskStaff 使用策略
public class FrontDeskStaff extends Person {
    private PricingStrategy pricingStrategy;
    
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    
    public double calculateRoomPrice(int nights) {
        return pricingStrategy.calculatePrice(nights);
    }
}
```

**識別 Divergent Change 的方法**:
1. 問自己：「這個類別會因為什麼原因而需要修改？」
2. 如果答案是多個不同的原因，就是 Divergent Change
3. 查看類別的方法，如果可以明顯分組（如薪資相關、定價相關、折扣相關），就該拆分

**重構步驟**:
1. 識別類別中不同的變更原因
2. 為每個變更原因建立新的類別
3. 將相關的方法和欄位移到新類別
4. 在原類別中使用組合（Composition）方式使用新類別
5. 執行測試確保行為不變

---

### 7. Data Class（資料類別）

**位置**: `Housekeeper.java` (整個類別)

**問題描述**: 
類別主要只包含資料欄位和 getter/setter 方法，缺乏有意義的業務邏輯行為。雖然有 `cleanRoom()` 和 `reportStatus()` 方法，但大部分程式碼仍是存取器。

**程式碼範例**:
```java
public class Housekeeper extends Person {
    private String assignedFloor;
    private int roomsCleaned;
    private boolean isAvailable;
    
    // 建構子
    public Housekeeper(...) { ... }
    
    // 大量的 getter/setter
    public String getAssignedFloor() { return assignedFloor; }
    public void setAssignedFloor(String assignedFloor) { ... }
    public int getRoomsCleaned() { return roomsCleaned; }
    public void setRoomsCleaned(int roomsCleaned) { ... }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { ... }
}
```

**為何有問題**:
- 類別僅作為資料容器，沒有封裝業務邏輯
- 其他類別可能直接操作這些資料，導致邏輯分散
- 違反物件導向的封裝原則
- 容易產生 Feature Envy 問題

**改善建議**:
```java
public class Housekeeper extends Person {
    private String assignedFloor;
    private int roomsCleaned;
    private boolean isAvailable;
    private List<CleaningTask> tasks;
    
    // 加入有意義的業務行為
    public void assignToFloor(String floor) {
        this.assignedFloor = floor;
        this.isAvailable = true;
    }
    
    public boolean canCleanRoom() {
        return isAvailable && roomsCleaned < MAX_ROOMS_PER_DAY;
    }
    
    public void completeCleaningTask(String roomNumber) {
        if (!canCleanRoom()) {
            throw new IllegalStateException("Housekeeper not available");
        }
        // 清潔邏輯
        roomsCleaned++;
        if (roomsCleaned >= MAX_ROOMS_PER_DAY) {
            isAvailable = false;
        }
    }
    
    public void startShift() {
        roomsCleaned = 0;
        isAvailable = true;
        tasks = loadTodaysTasks();
    }
    
    // 減少 setter，增加業務方法
}
```

---

### 8. Long Parameter List（過長參數列表）

**位置**: `Housekeeper.java` - `cleanRoom()` 方法 (第 19-20 行)

**問題描述**: 
方法有 6 個參數（roomNumber, deepClean, changeSheets, vacuumCarpet, cleanBathroom, restockSupplies），參數過多使方法難以使用和理解。

**程式碼範例**:
```java
public void cleanRoom(String roomNumber, boolean deepClean, boolean changeSheets, 
                     boolean vacuumCarpet, boolean cleanBathroom, boolean restockSupplies) {
    System.out.println("Cleaning room: " + roomNumber);
    if (deepClean) System.out.println("Performing deep clean");
    if (changeSheets) System.out.println("Changing sheets");
    if (vacuumCarpet) System.out.println("Vacuuming carpet");
    if (cleanBathroom) System.out.println("Cleaning bathroom");
    if (restockSupplies) System.out.println("Restocking supplies");
    roomsCleaned++;
}

// 呼叫時非常冗長且容易出錯
housekeeper.cleanRoom("101", true, true, true, true, true);
```

**為何有問題**:
- 參數太多，難以記憶參數順序
- 容易傳錯參數順序
- 方法簽名過長，降低可讀性
- 增加新的清潔選項時需要修改所有呼叫處

**改善建議**:

**方案 1：使用參數物件**
```java
// 建立清潔選項類別
public class CleaningOptions {
    private boolean deepClean;
    private boolean changeSheets;
    private boolean vacuumCarpet;
    private boolean cleanBathroom;
    private boolean restockSupplies;
    
    // 使用 Builder 模式建立
    public static class Builder {
        private boolean deepClean = false;
        private boolean changeSheets = false;
        private boolean vacuumCarpet = false;
        private boolean cleanBathroom = false;
        private boolean restockSupplies = false;
        
        public Builder deepClean() { this.deepClean = true; return this; }
        public Builder changeSheets() { this.changeSheets = true; return this; }
        public Builder vacuumCarpet() { this.vacuumCarpet = true; return this; }
        public Builder cleanBathroom() { this.cleanBathroom = true; return this; }
        public Builder restockSupplies() { this.restockSupplies = true; return this; }
        
        public CleaningOptions build() {
            return new CleaningOptions(this);
        }
    }
    
    private CleaningOptions(Builder builder) {
        this.deepClean = builder.deepClean;
        this.changeSheets = builder.changeSheets;
        this.vacuumCarpet = builder.vacuumCarpet;
        this.cleanBathroom = builder.cleanBathroom;
        this.restockSupplies = builder.restockSupplies;
    }
    
    // Getters...
}

// 簡化後的方法簽名
public void cleanRoom(String roomNumber, CleaningOptions options) {
    System.out.println("Cleaning room: " + roomNumber);
    if (options.isDeepClean()) System.out.println("Performing deep clean");
    if (options.isChangeSheets()) System.out.println("Changing sheets");
    // ...
}

// 使用時更清晰
CleaningOptions options = new CleaningOptions.Builder()
    .deepClean()
    .changeSheets()
    .vacuumCarpet()
    .cleanBathroom()
    .restockSupplies()
    .build();
    
housekeeper.cleanRoom("101", options);
```

**方案 2：使用預設的清潔類型**
```java
public enum CleaningType {
    STANDARD,
    DEEP_CLEAN,
    QUICK_CLEAN,
    CHECK_OUT_CLEAN
}

public void cleanRoom(String roomNumber, CleaningType type) {
    // 根據類型執行對應的清潔步驟
}
```

---

### 9. Primitive Obsession（基本型別偏執）

**位置**: `Chef.java` (第 10-12 行)

**問題描述**: 
過度使用基本型別（String）來表示複雜的領域概念，如 menuItems（菜單項目）、workSchedule（工作排程）、certifications（證照）。這些概念應該用專門的物件來表示。

**程式碼範例**:
```java
public class Chef extends Person {
    private String menuItems;        // 應該使用 Menu 或 List<MenuItem>
    private String workSchedule;     // 應該使用 Schedule 物件
    private String certifications;   // 應該使用 List<Certification>
}
```

**為何有問題**:
- 使用 String 儲存複雜資料，難以進行驗證和操作
- 失去型別安全性，容易出錯
- 無法利用物件導向的優勢
- 難以擴展功能

**改善建議**:
```java
// 建立專門的值物件（Value Objects）
public class MenuItem {
    private String name;
    private String description;
    private double price;
    private String category;
    private List<String> ingredients;
    
    public MenuItem(String name, double price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
    }
    
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty");
        }
    }
    
    private void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
    
    // 業務方法
    public boolean isVegetarian() { ... }
    public double getPriceWithTax() { ... }
}

public class Menu {
    private List<MenuItem> items;
    
    public void addItem(MenuItem item) { ... }
    public List<MenuItem> getItemsByCategory(String category) { ... }
    public MenuItem findMostExpensiveItem() { ... }
}

public class WorkSchedule {
    private LocalTime startTime;
    private LocalTime endTime;
    private Set<DayOfWeek> workDays;
    
    public boolean isWorkingOn(LocalDateTime dateTime) { ... }
    public int getWorkHoursPerWeek() { ... }
}

public class Certification {
    private String name;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    
    public boolean isValid() {
        return LocalDate.now().isBefore(expiryDate);
    }
}

// 改善後的 Chef 類別
public class Chef extends Person {
    private String specialty;
    private int yearsOfExperience;
    private Menu menu;                          // 使用物件
    private WorkSchedule schedule;              // 使用物件
    private List<Certification> certifications; // 使用物件列表
    
    public void addMenuItem(MenuItem item) {
        menu.addItem(item);
    }
    
    public boolean canWorkOn(LocalDateTime dateTime) {
        return schedule.isWorkingOn(dateTime);
    }
    
    public boolean hasValidCertifications() {
        return certifications.stream().allMatch(Certification::isValid);
    }
}
```

---

### 10. Dead Code（無用程式碼）

**位置**: `Chef.java` (第 21-25 行, 第 27-30 行)

**問題描述**: 
包含從未被呼叫的方法（`oldCookingMethod`、`unusedHelperMethod`），這些程式碼應該被移除。

**程式碼範例**:
```java
// 從未被呼叫的方法
private void oldCookingMethod() {
    System.out.println("This is an old cooking method, no longer used");
    // outdated logic...
}

private void unusedHelperMethod() {
    // Helper method never called
}
```

**為何有問題**:
- 增加程式碼量，降低可讀性
- 造成混淆，不知道這些方法是否還在使用
- 增加維護成本
- 可能包含過時的邏輯或安全漏洞
- 占用記憶體空間

**改善建議**:
```java
// 1. 使用 IDE 工具找出未使用的程式碼
// 2. 確認這些程式碼真的不需要
// 3. 使用版本控制系統（如 Git）保存歷史，然後刪除
// 4. 如果不確定，可以先標記為 @Deprecated

public class Chef extends Person {
    // 直接移除未使用的方法
    
    // 只保留實際使用的方法
    public void prepareMeal(String dishName, int quantity) {
        System.out.println("Preparing " + quantity + " portions of " + dishName);
    }
}

// 如果暫時不確定是否要移除，可以先標記
@Deprecated
@SuppressWarnings("unused")
private void possiblyUnusedMethod() {
    // 加上註解說明為何保留
    // TODO: Remove after confirming not used in version 2.0
}
```

---

### 11. Speculative Generality（過度設計）

**位置**: `Chef.java` - `prepareSpecialDiet()` 方法 (第 38-44 行)

**問題描述**: 
設計了過於複雜的特殊飲食準備功能，包含 6 個參數處理各種情況，但這些功能很可能永遠不會被完整使用。這是「為未來需求過度設計」的典型例子。

**程式碼範例**:
```java
// 過度複雜的方法，試圖處理所有可能的情況
public void prepareSpecialDiet(String dietType, String restrictions, 
                              String allergies, String preferences,
                              String cookingMethod, String presentation) {
    // Overly complex special diet preparation feature
    System.out.println("Preparing special diet");
}
```

**為何有問題**:
- YAGNI 原則（You Aren't Gonna Need It）：不要實作目前不需要的功能
- 增加程式碼複雜度
- 浪費開發時間
- 未來需求可能與現在的設計不同，導致需要重構
- 增加測試負擔

**改善建議**:
```java
// 方案 1：只實作目前需要的功能
public void prepareMeal(String dishName, int quantity) {
    System.out.println("Preparing " + quantity + " portions of " + dishName);
}

// 當真正需要特殊飲食功能時，再根據實際需求設計
// 例如，如果只需要處理過敏原：
public void prepareMeal(String dishName, int quantity, Set<Allergen> allergensToAvoid) {
    validateNoAllergens(dishName, allergensToAvoid);
    System.out.println("Preparing " + quantity + " allergen-free portions of " + dishName);
}

// 或使用策略模式處理不同的飲食類型
interface DietStrategy {
    void prepare(String dishName, int quantity);
}

class StandardDiet implements DietStrategy {
    public void prepare(String dishName, int quantity) { ... }
}

class VegetarianDiet implements DietStrategy {
    public void prepare(String dishName, int quantity) { ... }
}

// Chef 類別中
private DietStrategy dietStrategy = new StandardDiet();

public void prepareMeal(String dishName, int quantity) {
    dietStrategy.prepare(dishName, quantity);
}
```

**設計原則**:
- 遵循 YAGNI：只實作目前需要的功能
- 遵循 KISS（Keep It Simple, Stupid）：保持簡單
- 採用敏捷開發：根據實際需求逐步擴展
- 設計應該易於擴展，但不要過早優化

---

### 12. God Class（上帝類別）

**位置**: `Hotel.java` (整個類別)

**問題描述**: 
Hotel 類別承擔過多職責，管理房間、客人、前台員工、打掃人員、廚師等所有飯店相關業務，違反了單一職責原則。

**程式碼範例**:
```java
public class Hotel {
    // 管理所有類型的資源
    private List<Room> rooms;
    private List<Guest> guests;
    private List<FrontDeskStaff> frontDeskStaffs;
    private List<Housekeeper> housekeepers;
    private List<Chef> chefs;
    
    // 處理各種不同的業務
    public void addRoom(Room room) { ... }
    public void addGuest(Guest guest) { ... }
    public void addFrontDeskStaff(FrontDeskStaff staff) { ... }
    public void addHousekeeper(Housekeeper housekeeper) { ... }
    public void addChef(Chef chef) { ... }
    public void displayHotelInfo() { ... }
    public void printFirstGuestRoomInfo() { ... }
    public void manageAllServices(Guest guest, String service) { ... }
}
```

**為何有問題**:
- 違反單一職責原則，一個類別改變的理由太多
- 類別過於龐大，難以理解和維護
- 難以測試，需要模擬太多依賴
- 高耦合，修改一處可能影響其他功能
- 難以重用部分功能

**改善建議**:
```java
// 拆分成多個專責的管理類別

// 1. 房間管理
public class RoomManager {
    private List<Room> rooms;
    
    public void addRoom(Room room) { ... }
    public Room findAvailableRoom(String roomType) { ... }
    public List<Room> getAvailableRooms() { ... }
    public void updateRoomStatus(String roomNumber, boolean isOccupied) { ... }
}

// 2. 客人管理
public class GuestManager {
    private List<Guest> guests;
    
    public void registerGuest(Guest guest) { ... }
    public Guest findGuest(String guestId) { ... }
    public List<Guest> getCurrentGuests() { ... }
}

// 3. 員工管理
public class StaffManager {
    private List<FrontDeskStaff> frontDeskStaffs;
    private List<Housekeeper> housekeepers;
    private List<Chef> chefs;
    
    public void hireFrontDeskStaff(FrontDeskStaff staff) { ... }
    public void hireHousekeeper(Housekeeper housekeeper) { ... }
    public void hireChef(Chef chef) { ... }
    public FrontDeskStaff getAvailableFrontDeskStaff() { ... }
}

// 4. 服務管理
public class ServiceManager {
    private CheckInService checkInService;
    private CleaningService cleaningService;
    private DiningService diningService;
    
    public void handleCheckIn(Guest guest, String roomNumber, int nights) {
        checkInService.checkIn(guest, roomNumber, nights);
    }
    
    public void handleCleaning(String roomNumber) {
        cleaningService.cleanRoom(roomNumber);
    }
}

// 5. 簡化後的 Hotel 類別
public class Hotel {
    private String name;
    private String address;
    
    // 使用組合方式管理各個子系統
    private RoomManager roomManager;
    private GuestManager guestManager;
    private StaffManager staffManager;
    private ServiceManager serviceManager;
    
    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
        this.roomManager = new RoomManager();
        this.guestManager = new GuestManager();
        this.staffManager = new StaffManager();
        this.serviceManager = new ServiceManager();
    }
    
    // 委派給對應的管理器
    public void addRoom(Room room) {
        roomManager.addRoom(room);
    }
    
    public void registerGuest(Guest guest) {
        guestManager.registerGuest(guest);
    }
    
    public void checkInGuest(Guest guest, String roomNumber, int nights) {
        serviceManager.handleCheckIn(guest, roomNumber, nights);
    }
    
    public void displayHotelInfo() {
        System.out.println("Hotel: " + name);
        System.out.println("Address: " + address);
        System.out.println("Rooms: " + roomManager.getRoomCount());
        System.out.println("Guests: " + guestManager.getGuestCount());
        System.out.println("Staff: " + staffManager.getStaffCount());
    }
}
```

---

### 13. Message Chain（訊息鏈）

**位置**: `Hotel.java` - `printFirstGuestRoomInfo()` 方法 (第 34-43 行)

**問題描述**: 
方法通過長串的方法呼叫來取得資料（`guests.get(0).getRoomNumber()`），違反了迪米特法則（Law of Demeter），也稱為「最少知識原則」。

**程式碼範例**:
```java
public void printFirstGuestRoomInfo() {
    if (guests.size() > 0) {
        // 訊息鏈：Hotel -> Guest -> Room Number -> Room -> Room Type/Price
        String roomNum = guests.get(0).getRoomNumber();
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNum)) {
                System.out.println("Room type: " + room.getRoomType());
                System.out.println("Price: " + room.getPrice());
            }
        }
    }
}
```

**為何有問題**:
- 違反迪米特法則（一個物件應該對其他物件有最少的了解）
- 高耦合，Hotel 需要知道 Guest 和 Room 的內部結構
- 當中間任何一個類別的結構改變時，這段程式碼都需要修改
- 難以測試
- 可讀性差

**迪米特法則**:
一個物件應該只與以下物件互動：
1. 自己
2. 方法的參數
3. 方法內建立的物件
4. 物件的直接組件（欄位）

**改善建議**:

**方案 1：提供專門的查詢方法**
```java
// 在 Guest 類別中加入方法
public class Guest extends Person {
    private String roomNumber;
    
    public RoomInfo getRoomInfo(Hotel hotel) {
        return hotel.getRoomInfo(roomNumber);
    }
}

// 在 Hotel 類別中
public class Hotel {
    public RoomInfo getRoomInfo(String roomNumber) {
        Room room = findRoom(roomNumber);
        if (room != null) {
            return new RoomInfo(room.getRoomType(), room.getPrice());
        }
        return null;
    }
    
    private Room findRoom(String roomNumber) {
        return rooms.stream()
            .filter(r -> r.getRoomNumber().equals(roomNumber))
            .findFirst()
            .orElse(null);
    }
    
    // 簡化後的方法
    public void printFirstGuestRoomInfo() {
        if (!guests.isEmpty()) {
            Guest firstGuest = guests.get(0);
            RoomInfo info = firstGuest.getRoomInfo(this);
            if (info != null) {
                System.out.println("Room type: " + info.getType());
                System.out.println("Price: " + info.getPrice());
            }
        }
    }
}

// 建立資訊物件避免過度暴露
public class RoomInfo {
    private final String type;
    private final double price;
    
    public RoomInfo(String type, double price) {
        this.type = type;
        this.price = price;
    }
    
    public String getType() { return type; }
    public double getPrice() { return price; }
}
```

**方案 2：使用專門的查詢服務**
```java
public class HotelQueryService {
    private Hotel hotel;
    
    public RoomInfo getGuestRoomInfo(Guest guest) {
        String roomNumber = guest.getRoomNumber();
        Room room = hotel.findRoom(roomNumber);
        return new RoomInfo(room.getRoomType(), room.getPrice());
    }
}

// 使用
public void printFirstGuestRoomInfo() {
    if (!guests.isEmpty()) {
        RoomInfo info = queryService.getGuestRoomInfo(guests.get(0));
        System.out.println("Room type: " + info.getType());
        System.out.println("Price: " + info.getPrice());
    }
}
```

---

### 14. Refused Bequest（拒絕繼承）

**位置**: `Hotel.java` (整個類別，特別是第 7-9 行和第 36-46 行)

**問題描述**: 
子類別繼承父類別，但不使用或不適當使用父類別的方法和屬性。Hotel 類別繼承自 Person，但 Hotel 本質上不是一個「人」，因此被迫繼承了不適用的方法（如 `reportWorkSchedule()` 和 `calculatePerformanceBonus()`），卻拒絕正確實作這些方法。

**程式碼範例**:
```java
// Hotel 不應該是 Person 的子類別
public class Hotel extends Person {
    private String hotelName;
    private String address;
    
    public Hotel(String hotelName, String address) {
        // 被迫呼叫父類別建構子，傳入無意義的值
        super("Hotel Entity", 0, "N/A");
        this.hotelName = hotelName;
        this.address = address;
    }
    
    // 繼承了 reportWorkSchedule() 但沒有實作
    // Hotel 不應該有「工作時間表」這個概念
    @Override
    public void reportWorkSchedule() {
        // 空實作，拒絕使用父類別的方法
    }
    
    // 繼承了 calculatePerformanceBonus() 但沒有實作
    // Hotel 不應該有「績效獎金」這個概念
    @Override
    public double calculatePerformanceBonus() {
        // 回傳 0，拒絕實作父類別的方法
        return 0.0;
    }
}
```

**為何有問題**:
- 違反 Liskov 替換原則（Liskov Substitution Principle）
- 繼承關係不合理，Hotel 不是 Person 的一種
- 子類別被迫實作不需要的方法
- 增加類別的複雜度
- 破壞繼承的語意
- 容易產生誤解和錯誤使用

**Liskov 替換原則**:
子類別物件應該能夠替換掉父類別物件，而不會破壞程式的正確性。如果 Hotel 不能合理地作為 Person 使用，就不應該繼承 Person。

**識別 Refused Bequest**:
1. 子類別只使用父類別的一小部分功能
2. 子類別覆寫父類別的方法但留空或拋出異常
3. 繼承關係不符合「is-a」關係
4. 子類別需要繼承一些不相關的行為

**改善建議**:

**方案 1：移除不合理的繼承關係**
```java
// Hotel 不繼承 Person
public class Hotel {
    private String hotelName;
    private String address;
    private List<Room> rooms;
    private List<Guest> guests;
    private StaffManager staffManager;
    
    public Hotel(String hotelName, String address) {
        this.hotelName = hotelName;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.guests = new ArrayList<>();
        this.staffManager = new StaffManager();
    }
    
    // Hotel 特有的方法
    public void displayHotelInfo() {
        System.out.println("Hotel: " + hotelName);
        System.out.println("Address: " + address);
        // ...
    }
}
```

**方案 2：如果需要共用功能，使用組合而非繼承**
```java
// 如果 Hotel 和 Person 有共同的特徵，提取到介面
interface Identifiable {
    String getName();
    void setName(String name);
}

public class Person implements Identifiable {
    private String name;
    private int age;
    private String contactInfo;
    
    @Override
    public String getName() { return name; }
    
    @Override
    public void setName(String name) { this.name = name; }
    
    public void reportWorkSchedule() {
        System.out.println("Work schedule...");
    }
}

public class Hotel implements Identifiable {
    private String hotelName;
    private String address;
    
    @Override
    public String getName() { return hotelName; }
    
    @Override
    public void setName(String name) { this.hotelName = name; }
    
    // Hotel 不需要實作 reportWorkSchedule()
}
```

**方案 3：如果確實需要繼承，使用抽象類別**
```java
// 建立更通用的基礎類別
public abstract class Entity {
    private String id;
    private String name;
    
    public Entity(String name) {
        this.name = name;
        this.id = generateId();
    }
    
    protected abstract String generateId();
    
    public String getName() { return name; }
}

// Person 繼承 Entity 並加入特定行為
public class Person extends Entity {
    private int age;
    private String contactInfo;
    
    public Person(String name, int age, String contactInfo) {
        super(name);
        this.age = age;
        this.contactInfo = contactInfo;
    }
    
    @Override
    protected String generateId() {
        return "P-" + System.currentTimeMillis();
    }
    
    public void reportWorkSchedule() {
        System.out.println("Work schedule...");
    }
}

// Hotel 繼承 Entity 但不需要 Person 的特定行為
public class Hotel extends Entity {
    private String address;
    
    public Hotel(String name, String address) {
        super(name);
        this.address = address;
    }
    
    @Override
    protected String generateId() {
        return "H-" + System.currentTimeMillis();
    }
    
    // Hotel 特有的方法
}
```

**實際案例**:
```java
// ❌ 不好的設計
class Bird {
    void fly() { /* flying logic */ }
}

class Penguin extends Bird {
    @Override
    void fly() {
        throw new UnsupportedOperationException("Penguins can't fly!");
    }
}

// ✅ 好的設計
interface Animal {
    void move();
}

class Bird implements Animal {
    @Override
    public void move() {
        fly();
    }
    
    private void fly() { /* flying logic */ }
}

class Penguin implements Animal {
    @Override
    public void move() {
        swim();
    }
    
    private void swim() { /* swimming logic */ }
}
```

**設計原則**:
- **優先使用組合而非繼承**：如果只是需要重用某些功能，使用組合
- **確保 is-a 關係**：只有在子類別真的是父類別的一種時才使用繼承
- **遵循 Liskov 替換原則**：子類別應該能完全替換父類別
- **使用介面定義共同行為**：當多個不相關的類別有共同行為時，使用介面而非繼承

**檢查清單**:
- [ ] 子類別是否真的是父類別的一種（is-a 關係）？
- [ ] 子類別是否使用了父類別的大部分功能？
- [ ] 子類別是否能夠合理地替換父類別？
- [ ] 繼承關係是否符合業務邏輯和現實世界的關係？
- [ ] 如果只是為了重用程式碼，是否可以用組合代替？

---

### 15. Unsuitable Naming（不當命名）

**位置**: `Room.java` (第 13 行, 第 16 行, 第 65-77 行)

**問題描述**: 
使用不清楚、不一致或誤導性的變數名稱和方法名稱，使程式碼難以理解和維護。包括：過於簡短的縮寫、命名與實際功能不符、不遵循命名慣例等。

**程式碼範例**:
```java
public class Room {
    // 問題 1：不清楚的縮寫
    private int n;  // n 代表什麼？客人數量？預訂天數？
    
    // 問題 2：誤導性的名稱
    private String temp;  // temp 是暫存？還是溫度？
    
    // 問題 3：方法名稱沒有描述其功能
    public void doIt() {  // 做什麼？
        isOccupied = true;
        n++;
    }
    
    // 問題 4：不一致的命名慣例（應該用 camelCase）
    public void CheckAvailability() {  // 應該是 checkAvailability
        System.out.println("Checking availability...");
    }
    
    // 問題 5：方法名稱與實際功能不符
    public boolean validateRoom() {  // 實際上是回傳可用性，不是驗證
        return !isOccupied;
    }
}
```

**為何有問題**:
- 降低程式碼可讀性，需要花時間猜測變數或方法的用途
- 增加理解和維護成本
- 容易造成誤解和錯誤使用
- 團隊協作時造成溝通困難
- 不遵循 Java 命名慣例，與專案標準不一致

**命名問題類型**:

1. **過於簡短的縮寫**
   - ❌ `n`, `temp`, `flg`, `cnt`
   - ✅ `numberOfGuests`, `temperature`, `isAvailable`, `count`

2. **誤導性名稱**
   - ❌ `validateRoom()` 實際回傳可用性
   - ✅ `isAvailable()` 或 `checkAvailability()`

3. **無意義的名稱**
   - ❌ `doIt()`, `process()`, `handle()`
   - ✅ `markRoomAsOccupied()`, `processPayment()`, `handleCheckIn()`

4. **不一致的命名慣例**
   - ❌ `CheckAvailability()` (應該用 camelCase)
   - ✅ `checkAvailability()`

5. **技術性而非業務性的名稱**
   - ❌ `data`, `info`, `obj`
   - ✅ `guestInformation`, `roomDetails`, `reservation`

**改善建議**:
```java
public class Room {
    // 改善 1：使用清楚的名稱
    private int numberOfGuests;  // 清楚表達這是客人數量
    private int nightsBooked;    // 或者是預訂天數
    
    // 改善 2：使用描述性的名稱
    private String roomStatus;   // 如果是房間狀態
    private double temperature;  // 如果是溫度
    
    // 改善 3：方法名稱清楚描述功能
    public void markRoomAsOccupied() {
        isOccupied = true;
        numberOfGuests++;
    }
    
    // 改善 4：遵循 Java 命名慣例（camelCase）
    public void checkAvailability() {
        System.out.println("Checking room availability...");
    }
    
    // 改善 5：方法名稱符合實際功能
    public boolean isAvailable() {  // 或 checkIsAvailable()
        return !isOccupied;
    }
    
    // 其他良好的命名範例
    public void reserveRoomForGuest(Guest guest, int nights) {
        this.numberOfGuests = guest.getPartySize();
        this.nightsBooked = nights;
        this.isOccupied = true;
    }
    
    public boolean canAccommodateGuests(int guestCount) {
        return !isOccupied && guestCount <= maximumCapacity;
    }
}
```

**Java 命名慣例**:

1. **類別名稱**：使用 PascalCase（每個單字首字母大寫）
   - ✅ `Room`, `FrontDeskStaff`, `HotelManager`

2. **方法名稱**：使用 camelCase（第一個單字小寫，後續單字首字母大寫）
   - ✅ `checkAvailability()`, `markAsOccupied()`, `calculateTotal()`

3. **變數名稱**：使用 camelCase
   - ✅ `numberOfGuests`, `roomNumber`, `isAvailable`

4. **常數名稱**：使用 UPPER_SNAKE_CASE（全大寫，單字間用底線）
   - ✅ `MAX_GUESTS`, `DEFAULT_ROOM_RATE`, `MILLISECONDS_PER_DAY`

5. **Boolean 變數/方法**：使用 is/has/can 等前綴
   - ✅ `isAvailable`, `hasGuests`, `canCheckIn`

**命名最佳實踐**:

```java
// ❌ 不好的命名
public class Rm {
    private int n;
    private String t;
    private boolean f;
    
    public void do1() { }
    public boolean chk() { }
}

// ✅ 好的命名
public class Room {
    private int numberOfGuests;
    private String roomType;
    private boolean isOccupied;
    
    public void markRoomAsOccupied() { }
    public boolean isAvailableForBooking() { }
}

// 方法命名範例
// ❌ 不好
public void process() { }
public void handle() { }
public void doIt() { }

// ✅ 好
public void processPayment() { }
public void handleCheckInRequest() { }
public void updateRoomStatus() { }

// Boolean 方法命名
// ❌ 不好
public boolean available() { }
public boolean validate() { }

// ✅ 好
public boolean isAvailable() { }
public boolean hasValidReservation() { }
public boolean canAccommodateGuests(int count) { }
```

**檢查清單**:
- [ ] 變數名稱是否清楚表達其用途？
- [ ] 方法名稱是否描述其功能？
- [ ] 是否遵循 Java 命名慣例？
- [ ] Boolean 變數/方法是否使用 is/has/can 前綴？
- [ ] 常數是否使用 UPPER_SNAKE_CASE？
- [ ] 名稱是否避免使用縮寫（除非是廣為人知的縮寫如 URL, ID）？
- [ ] 名稱是否使用業務領域的術語而非技術術語？

---

## 總結與改善優先順序

### 高優先級（應立即修正）:
1. **Magic Number** - 容易修正，影響大
2. **Duplicate Code** - 降低維護成本
3. **Long Method** - 提高可讀性和可測試性
4. **Unsuitable Naming** - 改善程式碼可讀性

### 中優先級（應盡快修正）:
5. **Long Class** - 需要重構，但影響範圍大
6. **God Class** - 需要架構調整
7. **Switch Statement** - 改用設計模式
8. **Long Parameter List** - 使用參數物件

### 低優先級（可逐步改善）:
9. **Feature Envy** - 重新分配職責
10. **Data Class** - 加入業務邏輯
11. **Primitive Obsession** - 建立值物件
12. **Message Chain** - 提供查詢方法
13. **Refused Bequest** - 重構服務管理

### 維護性（持續注意）:
14. **Dead Code** - 定期清理
15. **Speculative Generality** - 遵循 YAGNI 原則

---

## 重構建議步驟

### 第一階段：快速改善（1-2 天）
1. 移除 Dead Code
2. 提取 Magic Number 為常數
3. 改善 Unsuitable Naming（重新命名變數和方法）

### 第二階段：方法級重構（3-5 天）
4. 拆分 Long Method
5. 消除 Duplicate Code
6. 使用參數物件替換 Long Parameter List
7. 解決 Divergent Change（拆分職責）

### 第三階段：類別級重構（1-2 週）
8. 解決 Feature Envy
9. 為 Data Class 加入行為
10. 建立值物件解決 Primitive Obsession

### 第四階段：架構級重構（2-4 週）
11. 拆分 Long Class 和 God Class
12. 簡化 Message Chain
13. 重構繼承關係（Refused Bequest）
14. 整體架構優化

---

## 教學目的

本專案刻意設計用於程式碼品質教育。透過這些具體的 code smell 範例，學生可以：

1. **識別能力**：學習如何在實際程式碼中發現這些問題
2. **理解能力**：理解為什麼這些是問題，會造成什麼影響
3. **實踐能力**：練習各種重構技巧和設計模式
4. **判斷能力**：學會判斷改善的優先順序
5. **設計能力**：提升軟體設計和架構能力

---

## 注意事項

⚠️ **警告**：此程式碼刻意包含不良實踐，僅供教學使用。請勿在正式專案中使用這些模式。

### 學習建議：
- 先嘗試自己找出 code smell
- 思考為什麼這樣設計不好
- 動手重構並比較改善前後的差異
- 撰寫單元測試驗證重構的正確性
- 使用版本控制記錄重構過程

### 延伸學習：
- 閱讀《Refactoring》by Martin Fowler
- 學習 SOLID 設計原則
- 研究 Design Patterns
- 練習 Test-Driven Development (TDD)
- 使用靜態分析工具（如 SonarQube, PMD, CheckStyle）
```java
// 建立專門的服務類別
public class CheckInService {
    private FrontDeskStaff staff;
    
    public void checkIn(Guest guest, String roomNumber, int nights) {
        validateStaff();
        staff.checkIn(guest, roomNumber, nights);
    }
    
    private void validateStaff() {
        if (staff == null) {
            throw new IllegalStateException("No staff available");
        }
    }
}

public class CleaningService {
    private List<Housekeeper> housekeepers;
    
    public void requestCleaning(String roomNumber, CleaningType type) {
        Housekeeper available = findAvailableHousekeeper();
        if (available != null) {
            available.cleanRoom(roomNumber, type);
        }
    }
    
    private Housekeeper findAvailableHousekeeper() {
        return housekeepers.stream()
            .filter(Housekeeper::isAvailable)
            .findFirst()
            .orElse(null);
    }
}

// Hotel 使用這些服務
public class Hotel {
    private CheckInService checkInService;
    private CheckOutService checkOutService;
    private CleaningService cleaningService;
    private DiningService diningService;
    
    public void checkInGuest(Guest guest, String roomNumber, int nights) {
        checkInService.checkIn(guest, roomNumber, nights);
    }
    
    public void checkOutGuest(Guest guest) {
        checkOutService.checkOut(guest);
    }
    
    public void requestRoomCleaning(String roomNumber) {
        cleaningService.requestCleaning(roomNumber, CleaningType.STANDARD);
    }
}
```

---

## 總結與改善優先順序

### 高優先級（應立即修正）:
1. **Magic Number** - 容易修正，影響大
2. **Duplicate Code** - 降低維護成本
3. **Long Method** - 提高可讀性和可測試性
4. **Unsuitable Naming** - 改善程式碼可讀性

### 中優先級（應盡快修正）:
5. **Long Class** - 需要重構，但影響範圍大
6. **God Class** - 需要架構調整
7. **Divergent Change** - 拆分職責
8. **Long Parameter List** - 使用參數物件

### 低優先級（可逐步改善）:
9. **Feature Envy** - 重新分配職責
10. **Data Class** - 加入業務邏輯
11. **Primitive Obsession** - 建立值物件
12. **Message Chain** - 提供查詢方法
13. **Refused Bequest** - 重構繼承關係

### 維護性（持續注意）:
14. **Dead Code** - 定期清理
15. **Speculative Generality** - 遵循 YAGNI 原則
11. **Primitive Obsession** - 建立值物件
12. **Message Chain** - 提供查詢方法
13. **Refused Bequest** - 重構服務管理

### 維護性（持續注意）:
14. **Dead Code** - 定期清理
15. **Speculative Generality** - 遵循 YAGNI 原則

---

## 重構建議步驟

### 第一階段：快速改善（1-2 天）
1. 移除 Dead Code
2. 提取 Magic Number 為常數
3. 封裝 public 欄位（Inappropriate Intimacy）

### 第二階段：方法級重構（3-5 天）
4. 拆分 Long Method
5. 消除 Duplicate Code
6. 使用參數物件替換 Long Parameter List

### 第三階段：類別級重構（1-2 週）
7. 解決 Feature Envy
8. 為 Data Class 加入行為
9. 替換 Switch Statement 為設計模式
10. 建立值物件解決 Primitive Obsession

### 第四階段：架構級重構（2-4 週）
11. 拆分 Long Class 和 God Class
12. 簡化 Message Chain
13. 重構服務管理（Refused Bequest）
14. 整體架構優化

---

## 教學目的

本專案刻意設計用於程式碼品質教育。透過這些具體的 code smell 範例，學生可以：

1. **識別能力**：學習如何在實際程式碼中發現這些問題
2. **理解能力**：理解為什麼這些是問題，會造成什麼影響
3. **實踐能力**：練習各種重構技巧和設計模式
4. **判斷能力**：學會判斷改善的優先順序
5. **設計能力**：提升軟體設計和架構能力

---

## 注意事項

⚠️ **警告**：此程式碼刻意包含不良實踐，僅供教學使用。請勿在正式專案中使用這些模式。

### 學習建議：
- 先嘗試自己找出 code smell
- 思考為什麼這樣設計不好
- 動手重構並比較改善前後的差異
- 撰寫單元測試驗證重構的正確性
- 使用版本控制記錄重構過程

### 延伸學習：
- 閱讀《Refactoring》by Martin Fowler
- 學習 SOLID 設計原則
- 研究 Design Patterns
- 練習 Test-Driven Development (TDD)
- 使用靜態分析工具（如 SonarQube, PMD, CheckStyle）

### 1. Long Class（過長類別）
**位置**: `FrontDeskStaff.java`
**問題**: 類別承擔過多職責，違反單一職責原則（Single Responsibility Principle）。
**說明**: FrontDeskStaff 類別包含太多方法和職責，應該拆分成多個獨立的類別。

### 2. Magic Number（魔術數字）
**位置**: `FrontDeskStaff.java`（baseSalary 和 workHours 定義處）
```java
private double baseSalary = 30000; // 應使用具名常數
private int workHours = 8; // 應使用具名常數
```
**問題**: 直接使用數字字面值，缺乏語義意義。
**說明**: 這些值應該提取為具名常數，例如：`private static final double BASE_SALARY = 30000;`

### 3. Long Method（過長方法）
**位置**: `FrontDeskStaff.java` - `checkIn()` 方法
**問題**: 方法包含過多邏輯，應該拆分為更小的方法。
**說明**: checkIn 方法同時處理驗證、賦值、計算和輸出；每個功能都應該被提取出來。

### 4. Duplicate Code（重複程式碼）
**位置**: `FrontDeskStaff.java` - `checkIn()` 和 `checkOut()` 方法
**問題**: 驗證和計算邏輯有大量重複。
**說明**: 應提取共用的驗證和費用計算程式碼。

### 5. Feature Envy（特性依戀）
**位置**: `FrontDeskStaff.java` - `printGuestDetails()` 方法
**問題**: 方法過度使用另一個物件的資料；這個方法應該屬於 `Guest` 類別。

### 6. Switch Statement（複雜的 Switch 語句）
**位置**: `FrontDeskStaff.java` - `calculateSalary()` 方法
**問題**: 使用 switch 語句；應考慮使用策略模式（Strategy pattern）或多型（polymorphism）。

### 7. Data Class（資料類別）
**位置**: `Housekeeper.java`
**問題**: 主要只有 getter/setter 和資料；缺乏有意義的行為。

### 8. Long Parameter List（過長參數列表）
**位置**: `Housekeeper.java` - `cleanRoom()` 方法
**問題**: 參數過多；應使用參數物件（例如：`CleaningOptions`）。

### 9. Primitive Obsession（基本型別偏執）
**位置**: `Chef.java`
**問題**: 過度使用基本型別表示領域概念（menuItems、workSchedule、certifications）。

### 10. Dead Code（無用程式碼）
**位置**: `Chef.java`
**問題**: 從未被呼叫的方法（`oldCookingMethod`、`unusedHelperMethod`）應該被移除。

### 11. Speculative Generality（過度設計）
**位置**: `Chef.java` - `prepareSpecialDiet()` 方法
**問題**: 過於複雜和推測性的功能，很可能永遠不會被完整使用。

### 12. God Class（上帝類別）
**位置**: `Hotel.java`
**問題**: 類別承擔過多職責，管理房間、客人和所有員工。

### 13. Inappropriate Intimacy（不當親密關係）
**位置**: `Hotel.java`（公開欄位 `currentStaff`、`currentHousekeeper`）
**問題**: 過度暴露內部狀態；應該設為私有並提供存取方法。

### 14. Message Chain（訊息鏈）
**位置**: `Hotel.java` - `printFirstGuestRoomInfo()` 方法
**問題**: 過長的呼叫鏈違反了迪米特法則（Law of Demeter）；應提供直接的方法。

### 15. Refused Bequest 變體 / 違反單一職責原則
**位置**: `Hotel.java` - `manageAllServices()` 方法
**問題**: 一個方法使用大量 if-else 分支處理所有服務類型；應該委派給專門的管理器。

## 如何改進

### 建議的重構步驟：

1. **提取常數**：將魔術數字替換為具名常數。
2. **拆分長方法**：將大型方法拆分成單一職責的輔助方法。
3. **提取共用邏輯**：將重複的程式碼移到共用方法中。
4. **使用設計模式**：
    - 使用策略模式替換 switch 語句
    - 使用參數物件替換過長的參數列表
5. **建立值物件**：將基本型別包裝成領域型別。
6. **移除無用程式碼**：刪除未使用的方法和欄位。
7. **簡化設計**：移除推測性功能，直到真正需要時再加入。
8. **拆分上帝類別**：為不同職責建立管理類別。
9. **封裝欄位**：將欄位設為私有並提供適當的存取方法。
10. **遵循迪米特法則**：減少物件之間的耦合。

## 教學目的

本專案刻意設計用於程式碼品質教育。每個 code smell 都有標註和文件說明。學生可以：
1. 識別這些 code smell
2. 理解它們為何有問題
3. 練習重構技巧以改善程式碼品質

## 注意事項

⚠️ **警告**：此程式碼刻意包含不良實踐，僅供教學使用。請勿在正式專案中使用這些模式。
