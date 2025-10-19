package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

public class Chef extends Person {
    private String specialty;
    private int yearsOfExperience;
    // CODE SMELL 9: Primitive Obsession
    // Issue: Overuse of primitives, should encapsulate with objects
    private String menuItems; // Should use MenuItem or Menu object
    private String workSchedule; // Should use Schedule object
    private String certifications; // Should use Certification object list

    public Chef(String name, int age, String contactInfo, String specialty) {
        super(name, age, contactInfo);
        this.specialty = specialty;
    }

    // CODE SMELL 10: Dead Code
    // Issue: This method is never used and should be removed
    private void oldCookingMethod() {
        System.out.println("This is an old cooking method, no longer used");
        // outdated logic...
    }

    // CODE SMELL 10: Another example
    private void unusedHelperMethod() {
        // Helper method never called
    }

    public void prepareMeal(String dishName, int quantity) {
        System.out.println("Preparing " + quantity + " portions of " + dishName);
        // cooking logic
    }

    // CODE SMELL 11: Speculative Generality
    // Issue: Over-designed, includes complex features that might never be needed
    public void prepareSpecialDiet(String dietType, String restrictions, 
                                  String allergies, String preferences,
                                  String cookingMethod, String presentation) {
        // Overly complex special diet preparation feature, may never be fully utilized
        System.out.println("Preparing special diet");
    }

    public void displaySpecialty() {
        System.out.println("Chef: " + getName());
        System.out.println("Specialty: " + specialty);
        System.out.println("Years of experience: " + yearsOfExperience);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(String menuItems) {
        this.menuItems = menuItems;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }
}