package com.hotel.service;

// Unnecessary Complexity
// This class design is overly complex, should be simplified for better readability and maintainability
public class HousekeepingService {

    // Inconsistent Naming
    // Method name is inconsistent with its functionality, should be changed to a more descriptive name
    public void cleanRoom(int roomNumber) {
        // Magic Numbers
        // These numbers should be replaced with constants for better readability
        if (roomNumber < 1 || roomNumber > 100) {
            throw new IllegalArgumentException("Invalid room number");
        }
        // Room cleaning logic
        System.out.println("Cleaning room " + roomNumber);
    }

    // Excessive Responsibility
    // This method has too many responsibilities, should be split into multiple methods
    public void manageHousekeeping() {
        // Logic here is too complex, should be split
        cleanRoom(1);
        cleanRoom(2);
        // Other cleaning logic
    }

    // Large Class
    // This class has too much functionality, should consider splitting
    public void scheduleCleaning(int roomNumber, String time) {
        // Logic here is too complex, should be split
        System.out.println("Scheduled cleaning for room " + roomNumber + " at " + time);
    }

    // Excessive Static Methods
    // If there is state to manage, avoid using static methods
    public static void printCleaningStatus() {
        System.out.println("All rooms are clean.");
    }

    // Long Method
    // This method is too long, should be split into smaller methods
    public void performDailyCleaning() {
        for (int i = 1; i <= 100; i++) {
            cleanRoom(i);
        }
        // Other cleaning logic
    }
}