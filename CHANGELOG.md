# 專案修改紀錄

## 2024-10-19：Code Smell 更新

### 修改內容

#### 1. 移除 Code Smell #6: Switch Statement
**原本位置**: `FrontDeskStaff.java` - `calculateSalary()` 方法

**原本問題**: 使用 switch 語句根據職位計算薪資

#### 2. 新增 Code Smell #6: Divergent Change（發散式變化）
**新位置**: `FrontDeskStaff.java`

**問題描述**: 
- 類別因多種不同原因需要修改
- 包含薪資政策、定價政策、折扣政策等不同業務邏輯
- 違反單一職責原則

**新增欄位**:
```java
private double seniorStaffBonus = 15000;
private double managerBonus = 30000;
private double directorBonus = 50000;
private String peakSeasonSurcharge = "20%";
private String loyaltyDiscountRate = "10%";
```

**新增方法**:
- `calculateSalaryWithBonus(String position)` - 計算薪資與獎金
- `calculateRoomPrice(int nights, boolean isPeakSeason)` - 計算房價
- `applyLoyaltyDiscount(double amount, boolean isLoyalMember)` - 應用折扣

#### 3. 修改 Code Smell #14: Refused Bequest（拒絕繼承）
**原本**: `Hotel.java` - `manageAllServices()` 方法的變體

**修改為**: 真正的 Refused Bequest - 繼承關係問題

**新設計**:
- `Hotel` 類別現在繼承自 `Person` 類別（不合理的繼承）
- 被迫實作父類別的方法但不適用
- 展示子類別「拒絕」使用父類別功能的問題

**Person.java 新增方法**:
```java
public void reportWorkSchedule() {
    System.out.println("Work schedule not specified");
}

public double calculatePerformanceBonus() {
    return 0.0;
}
```

**Hotel.java 修改**:
```java
public class Hotel extends Person {
    // 被迫呼叫父類別建構子
    super("Hotel Entity", 0, "N/A");
    
    // 空實作 - 拒絕使用父類別方法
    @Override
    public void reportWorkSchedule() { }
    
    @Override
    public double calculatePerformanceBonus() {
        return 0.0;
    }
}
```

### 文件更新

#### CODE_SMELL_DOCUMENTATION.md
- **第 6 項**: Switch Statement → Divergent Change
  - 完整的問題描述和範例
  - 兩種改善方案（職責拆分、策略模式）
  - 識別方法和重構步驟

- **第 14 項**: Refused Bequest 變體 → Refused Bequest
  - 真正的繼承問題範例
  - Liskov 替換原則說明
  - 三種改善方案（移除繼承、使用組合、使用抽象類別）
  - 實際案例（Bird/Penguin 範例）

- **總結部分**: 更新優先順序，反映新的 code smell

#### Main.java
- 更新測試程式碼以展示新的 code smell
- 新增 Divergent Change 示範
- 新增 Refused Bequest 示範

### 編譯與測試

✅ 所有檔案編譯成功
✅ 程式執行正常
✅ 所有 15 個 Code Smell 都有清楚的範例

### Code Smell 列表（更新後）

1. ✅ Long Class（過長類別）- FrontDeskStaff.java
2. ✅ Magic Number（魔術數字）- FrontDeskStaff.java
3. ✅ Long Method（過長方法）- FrontDeskStaff.java
4. ✅ Duplicate Code（重複程式碼）- FrontDeskStaff.java
5. ✅ Feature Envy（特性依戀）- FrontDeskStaff.java
6. ✅ **Divergent Change（發散式變化）** - FrontDeskStaff.java ⭐ 新增
7. ✅ Data Class（資料類別）- Housekeeper.java
8. ✅ Long Parameter List（過長參數列）- Housekeeper.java
9. ✅ Primitive Obsession（基本類型偏執）- Chef.java
10. ✅ Dead Code（死碼）- Chef.java
11. ✅ Speculative Generality（投機性概括）- Chef.java
12. ✅ God Class（上帝類別）- Hotel.java
13. ✅ Message Chain（訊息鏈）- Hotel.java
14. ✅ **Refused Bequest（拒絕繼承）** - Hotel.java ⭐ 修改
15. ✅ Unsuitable Naming（不當命名）- Room.java

### 修改原因

1. **Switch Statement → Divergent Change**: 
   - Divergent Change 更能展示單一職責原則的違反
   - 更貼近實際專案中常見的問題
   - 提供更好的教學價值

2. **Refused Bequest 變體 → Refused Bequest**:
   - 原本的實作不是真正的 Refused Bequest
   - 新實作展示真正的繼承問題
   - 符合 Liskov 替換原則的教學

### 學習重點

#### Divergent Change
- 識別：一個類別因為多種原因需要修改
- 原因：違反單一職責原則
- 解決：拆分類別，每個類別只有一個變更原因

#### Refused Bequest
- 識別：子類別不使用父類別的方法
- 原因：繼承關係不合理（is-a 關係不成立）
- 解決：移除繼承、使用組合、或重新設計繼承結構
