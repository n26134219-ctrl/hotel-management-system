package com.hotel;

import com.hotel.model.*;

public class Main {
    public static void main(String[] args) {
        // Create hotel
        Hotel hotel = new Hotel("Five Star Grand Hotel", "100 Zhongxiao E Rd, Xinyi District, Taipei");

        // Create rooms
        Room room1 = new Room("101", "Standard Double Room", 2000.0);
        Room room2 = new Room("201", "Deluxe Suite", 5000.0);
        Room room3 = new Room("301", "Presidential Suite", 10000.0);
        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);

        // Create staff
        FrontDeskStaff staff = new FrontDeskStaff("John Zhang", 28, "0912-345-678", 
                                                  "Morning Shift", new String[]{"Check-in", "Check-out", "Customer Service"});
        Housekeeper housekeeper = new Housekeeper("Lisa Lee", 35, "0923-456-789", "1st Floor");
        Chef chef = new Chef("Master Wang", 45, "0934-567-890", "Chinese Cuisine");
        chef.setYearsOfExperience(20);

        hotel.addFrontDeskStaff(staff);
        hotel.addHousekeeper(housekeeper);
        hotel.addChef(chef);

        // Create guests
        Guest guest1 = new Guest("Mr. Chen", 30, "chen@example.com", "G001");
        Guest guest2 = new Guest("Ms. Lin", 28, "lin@example.com", "G002");
        hotel.addGuest(guest1);
        hotel.addGuest(guest2);

        // display hotel information
        System.out.println("=== Hotel Management System ===\n");
        hotel.displayHotelInfo();

        // test various features
        System.out.println("\n=== Testing hotel services ===\n");
        
        // test check-in (demonstrates Long Method and Magic Number)
        staff.checkIn(guest1, "101", 3);
        System.out.println();

        // test cleaning service (demonstrates Long Parameter List)
        housekeeper.cleanRoom("101", true, true, true, true, true);
        System.out.println();

        // test dining service
        chef.prepareMeal("Kung Pao Chicken", 2);
        System.out.println();

        // test check-out (demonstrates Duplicate Code)
        staff.checkOut(guest1);
        System.out.println();

        // test Feature Envy
        staff.printGuestDetails(guest2);
        System.out.println();

        // test Divergent Change (replaced Switch Statement)
        System.out.println("Junior salary: " + staff.calculateSalaryWithBonus("junior"));
        System.out.println("Senior salary: " + staff.calculateSalaryWithBonus("senior"));
        System.out.println("Manager salary: " + staff.calculateSalaryWithBonus("manager"));
        System.out.println("Peak season price (3 nights): " + staff.calculateRoomPrice(3, true));
        System.out.println("Loyalty discount on $5000: " + staff.applyLoyaltyDiscount(5000, true));
        System.out.println();

        // test Message Chain
        hotel.printFirstGuestRoomInfo();
        System.out.println();

        // test God Class's unified service management
        hotel.manageAllServices(guest2, "checkin");
        System.out.println();

        // test Refused Bequest - Hotel inherits from Person but doesn't implement properly
        System.out.println("=== Testing Refused Bequest ===");
        System.out.println("Hotel name: " + hotel.getHotelName());
        hotel.reportWorkSchedule(); // Inherited but not implemented properly
        System.out.println("Hotel performance bonus: " + hotel.calculatePerformanceBonus()); // Returns 0
        System.out.println();
    }
}