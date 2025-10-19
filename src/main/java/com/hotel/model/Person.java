package com.hotel.model;

// Person class definition, contains name, age and contact information
public class Person {
    private String name; // Name
    private int age; // Age
    private String contactInfo; // Contact information

    // Constructor
    public Person(String name, int age, String contactInfo) {
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
    }

    // Get name
    public String getName() {
        return name;
    }

    // Set name
    public void setName(String name) {
        this.name = name;
    }

    // Get age
    public int getAge() {
        return age;
    }

    // Set age
    public void setAge(int age) {
        this.age = age;
    }

    // Get contact information
    public String getContactInfo() {
        return contactInfo;
    }

    // Set contact information
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // Long method demonstration, possible code smell issue: Long Method
    public void displayPersonInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Contact: " + contactInfo);
        // More display logic could be added here, causing method to become too long
    }
    
    // Method intended for subclasses to override
    // Provides work schedule reporting functionality
    public void reportWorkSchedule() {
        System.out.println("Work schedule not specified");
    }
    
    // Method intended for subclasses to override
    // Calculates performance bonus
    public double calculatePerformanceBonus() {
        return 0.0;
    }
} 

// Code Smell annotations:
// 1. Long Method - displayPersonInfo() method is too long, should be split into multiple methods for better readability.