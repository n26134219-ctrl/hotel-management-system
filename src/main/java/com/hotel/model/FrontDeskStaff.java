package com.hotel.model;

import java.util.Date;

// CODE SMELL 1: Long Class
// Issue: Class has too many responsibilities, violating Single Responsibility Principle
public class FrontDeskStaff extends Person {
    private String shift;
    private String[] responsibilities;
    // CODE SMELL 2: Magic Number
    // Issue: Numeric literals used directly without named constants
    private double baseSalary = 30000; // Should use named constants
    private int workHours = 8; // Should use named constants
    
    // CODE SMELL 6: Divergent Change
    // Issue: Class needs modification for multiple different reasons (salary changes, shift changes, pricing changes, date calculation changes)
    // When business rules change, this class is modified for various unrelated reasons
    private double seniorStaffBonus = 15000; // Changes when senior bonus policy changes
    private double managerBonus = 30000; // Changes when manager bonus policy changes
    private double directorBonus = 50000; // Changes when director bonus policy changes
    private String peakSeasonSurcharge = "20%"; // Changes when pricing policy changes
    private String loyaltyDiscountRate = "10%"; // Changes when discount policy changes
    
    public FrontDeskStaff(String name, int age, String contactInfo, String shift, String[] responsibilities) {
        super(name, age, contactInfo);
        this.shift = shift;
        this.responsibilities = responsibilities;
    }

    // CODE SMELL 3: Long Method
    // Issue: Method contains too much logic and should be split into smaller methods
    public void checkIn(Guest guest, String roomNumber, int nights) {
        System.out.println("=== Begin check-in ===");
        System.out.println("Guest name: " + guest.getName());
        System.out.println("Room number: " + roomNumber);
        
        // Validate guest information
        if (guest.getName() == null || guest.getName().isEmpty()) {
            System.out.println("Error: Guest name cannot be empty");
            return;
        }
        
    // Set room
        guest.setRoomNumber(roomNumber);
    // set check-in date
        Date now = new Date();
        guest.setCheckInDate(now);
    // calculate check-out date - uses magic number
        Date checkOut = new Date(now.getTime() + nights * 24 * 60 * 60 * 1000);
        guest.setCheckOutDate(checkOut);
    // calculate cost - uses magic number
        double roomRate = 2000;
        double totalCost = roomRate * nights;
        // print receipt
        System.out.println("Nights: " + nights);
        System.out.println("Rate per night: " + roomRate);
        System.out.println("Total cost: " + totalCost);
        System.out.println("=== Check-in completed ===");
    }

    // CODE SMELL 4: Duplicate Code
    // Issue: Large overlap with checkIn method validation and calculation logic
    public void checkOut(Guest guest) {
        System.out.println("=== Begin check-out ===");
        System.out.println("Guest name: " + guest.getName());
        System.out.println("Room number: " + guest.getRoomNumber());
        
        // duplicated validation logic
        if (guest.getName() == null || guest.getName().isEmpty()) {
            System.out.println("Error: Guest name cannot be empty");
            return;
        }
        
        Date checkIn = guest.getCheckInDate();
        Date checkOut = new Date();
        long diff = checkOut.getTime() - checkIn.getTime();
        long days = diff / (24 * 60 * 60 * 1000); // duplicated magic number

        double roomRate = 2000; // duplicated magic number
        double totalCost = roomRate * days;

        System.out.println("Actual nights stayed: " + days);
        System.out.println("Total cost: " + totalCost);
        System.out.println("=== Check-out completed ===");
    }

    // CODE SMELL 5: Feature Envy
    // Issue: Overuse of another class's data; this should belong in Guest
    public void printGuestDetails(Guest guest) {
        System.out.println("Name: " + guest.getName());
        System.out.println("Age: " + guest.getAge());
        System.out.println("Contact: " + guest.getContactInfo());
        System.out.println("Room number: " + guest.getRoomNumber());
        System.out.println("Check-in date: " + guest.getCheckInDate());
    }

    // Modified to demonstrate Divergent Change instead of Switch Statement
    // This class changes for multiple reasons: salary policy, pricing policy, discount policy
    public double calculateSalaryWithBonus(String position) {
        // Changes when base salary policy changes
        double salary = baseSalary;
        
        // Changes when bonus policy changes
        if (position.equals("senior")) {
            salary += seniorStaffBonus;
        } else if (position.equals("manager")) {
            salary += managerBonus;
        } else if (position.equals("director")) {
            salary += directorBonus;
        }
        
        return salary;
    }
    
    // Changes when pricing policy changes
    public double calculateRoomPrice(int nights, boolean isPeakSeason) {
        double basePrice = 2000 * nights;
        if (isPeakSeason) {
            basePrice *= 1.2; // 20% surcharge
        }
        return basePrice;
    }
    
    // Changes when discount policy changes
    public double applyLoyaltyDiscount(double amount, boolean isLoyalMember) {
        if (isLoyalMember) {
            return amount * 0.9; // 10% discount
        }
        return amount;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String[] getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String[] responsibilities) {
        this.responsibilities = responsibilities;
    }
}