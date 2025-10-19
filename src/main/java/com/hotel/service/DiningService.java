package com.hotel.service;

import java.util.ArrayList;
import java.util.List;

// Code Smell: Long Method
public class DiningService {
    private List<String> menuItems; // Code Smell: Data Class

    public DiningService() {
        this.menuItems = new ArrayList<>();
        initializeMenu(); // Code Smell: Excessive Responsibility
    }

    // Code Smell: Long Method
    private void initializeMenu() {
        menuItems.add("Pasta");
        menuItems.add("Pizza");
        menuItems.add("Salad");
        menuItems.add("Steak");
        menuItems.add("Dessert");
    }

    // Code Smell: Inconsistent Naming
    public void addMenuItem(String item) {
        menuItems.add(item);
    }

    // Code Smell: Inconsistent Naming
    public void removeMenuItem(String item) {
        menuItems.remove(item);
    }

    // Code Smell: Long Method
    public void printMenu() {
        System.out.println("Menu:");
        for (String item : menuItems) {
            System.out.println("- " + item);
        }
    }

    // Code Smell: Excessive Getter/Setter
    public List<String> getMenuItems() {
        return menuItems;
    }

    // Code Smell: Excessive Static Methods
    public static void serveFood(String food) {
        System.out.println("Serving " + food);
    }

    // Code Smell: Duplicate Code
    public void serveAll() {
        for (String item : menuItems) {
            serveFood(item);
        }
    }
}