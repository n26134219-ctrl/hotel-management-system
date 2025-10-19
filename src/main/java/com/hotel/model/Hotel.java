package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

// CODE SMELL 12: God Class
// Issue: Class takes too many responsibilities, managing all hotel-related business
// CODE SMELL 14: Refused Bequest
// Issue: Hotel extends Person but doesn't use inherited methods meaningfully
// Hotel is not really a "Person" but inherits from Person anyway, refusing to implement parent methods properly
public class Hotel extends Person {
    private String hotelName;
    private String address;
    private List<Room> rooms;
    private List<Guest> guests;
    private List<FrontDeskStaff> frontDeskStaffs;
    private List<Housekeeper> housekeepers;
    private List<Chef> chefs;

    public Hotel(String hotelName, String address) {
        // Forced to call parent constructor even though Hotel is not a Person
        super("Hotel Entity", 0, "N/A");
        this.hotelName = hotelName;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.guests = new ArrayList<>();
        this.frontDeskStaffs = new ArrayList<>();
        this.housekeepers = new ArrayList<>();
        this.chefs = new ArrayList<>();
    }
    
    // Refused Bequest: Inherits reportWorkSchedule() but doesn't implement it properly
    // Hotel shouldn't have a "work schedule" like a Person
    @Override
    public void reportWorkSchedule() {
        // Does nothing meaningful - just refuses to use parent's method
        // This demonstrates refusing the bequest from parent class
    }
    
    // Refused Bequest: Inherits calculatePerformanceBonus() but doesn't implement it
    // Hotel shouldn't have a "performance bonus" like a Person
    @Override
    public double calculatePerformanceBonus() {
        // Returns 0 - refuses to implement parent's method properly
        return 0.0;
    }

    // CODE SMELL 13: Message Chain
    // Issue: Long method-call chain violating the Law of Demeter
    public void printFirstGuestRoomInfo() {
        if (guests.size() > 0) {
            String roomNum = guests.get(0).getRoomNumber();
            for (Room room : rooms) {
                if (room.getRoomNumber().equals(roomNum)) {
                    System.out.println("Room type: " + room.getRoomType());
                    System.out.println("Price: " + room.getPrice());
                }
            }
        }
    }

    // Removed: Old CODE SMELL 14 (variant of Refused Bequest)
    // Replaced with proper Refused Bequest in class inheritance
    public void manageAllServices(Guest guest, String service) {
        if (service.equals("checkin")) {
            if (frontDeskStaffs.size() > 0) {
                frontDeskStaffs.get(0).checkIn(guest, "101", 3);
            }
        } else if (service.equals("checkout")) {
            if (frontDeskStaffs.size() > 0) {
                frontDeskStaffs.get(0).checkOut(guest);
            }
        } else if (service.equals("clean")) {
            if (housekeepers.size() > 0) {
                housekeepers.get(0).cleanRoom("101", true, true, true, true, true);
            }
        } else if (service.equals("meal")) {
            if (chefs.size() > 0) {
                chefs.get(0).prepareMeal("Steak", 1);
            }
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public void addFrontDeskStaff(FrontDeskStaff staff) {
        frontDeskStaffs.add(staff);
    }

    public void addHousekeeper(Housekeeper housekeeper) {
        housekeepers.add(housekeeper);
    }

    public void addChef(Chef chef) {
        chefs.add(chef);
    }

    public void displayHotelInfo() {
        System.out.println("Hotel name: " + hotelName);
        System.out.println("Address: " + address);
        System.out.println("Total rooms: " + rooms.size());
        System.out.println("Total guests: " + guests.size());
        System.out.println("Front desk staff count: " + frontDeskStaffs.size());
        System.out.println("Housekeeper count: " + housekeepers.size());
        System.out.println("Chef count: " + chefs.size());
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getAddress() {
        return address;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public List<FrontDeskStaff> getFrontDeskStaffs() {
        return frontDeskStaffs;
    }

    public List<Housekeeper> getHousekeepers() {
        return housekeepers;
    }

    public List<Chef> getChefs() {
        return chefs;
    }
}