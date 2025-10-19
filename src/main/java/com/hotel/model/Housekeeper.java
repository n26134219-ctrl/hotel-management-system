package com.hotel.model;

// CODE SMELL 7: Data Class
// Issue: Class mainly holds data and getters/setters, lacking meaningful behavior
public class Housekeeper extends Person {
    private String assignedFloor;
    private int roomsCleaned;
    private boolean isAvailable;

    public Housekeeper(String name, int age, String contactInfo, String assignedFloor) {
        super(name, age, contactInfo);
        this.assignedFloor = assignedFloor;
        this.roomsCleaned = 0;
        this.isAvailable = true;
    }

    // CODE SMELL 8: Long Parameter List
    // Issue: Too many parameters, should use a parameter object
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

    public void reportStatus() {
        System.out.println("Housekeeper: " + getName());
        System.out.println("Assigned floor: " + assignedFloor);
        System.out.println("Rooms cleaned: " + roomsCleaned);
        System.out.println("Is available: " + isAvailable);
    }

    public String getAssignedFloor() {
        return assignedFloor;
    }

    public void setAssignedFloor(String assignedFloor) {
        this.assignedFloor = assignedFloor;
    }

    public int getRoomsCleaned() {
        return roomsCleaned;
    }

    public void setRoomsCleaned(int roomsCleaned) {
        this.roomsCleaned = roomsCleaned;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}