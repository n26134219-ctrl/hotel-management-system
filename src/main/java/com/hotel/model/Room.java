package com.hotel.model;

// CODE SMELL 15: Unsuitable Naming
// Issue: Variable and method names are unclear, inconsistent or misleading
public class Room {
    private String roomNumber;
    private String roomType;
    private double price;
    private boolean isOccupied;
    private boolean isClean;
    
    // CODE SMELL 15: Unsuitable Naming - unclear abbreviation
    private int n;  // What does 'n' mean? Should be 'numberOfGuests' or 'nightsBooked'
    
    // CODE SMELL 15: Unsuitable Naming - misleading name
    private String temp;  // Temporary? Temperature? Should have a clear purpose

    public Room(String roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isOccupied = false;
        this.isClean = true;
    }

    public void displayRoomInfo() {
        System.out.println("Room number: " + roomNumber);
        System.out.println("Room type: " + roomType);
        System.out.println("Price: $" + price);
        System.out.println("Occupied: " + isOccupied);
        System.out.println("Clean: " + isClean);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }
    
    // CODE SMELL 15: Unsuitable Naming - method name doesn't describe what it does
    public void doIt() {  // What does this method do? Should be 'markRoomAsOccupied' or similar
        isOccupied = true;
        n++;
    }
    
    // CODE SMELL 15: Unsuitable Naming - inconsistent naming convention
    public void CheckAvailability() {  // Should be 'checkAvailability' (camelCase)
        System.out.println("Checking availability...");
    }
    
    // CODE SMELL 15: Unsuitable Naming - name doesn't match functionality
    public boolean validateRoom() {  // Actually returns availability, not validation
        return !isOccupied;
    }
}