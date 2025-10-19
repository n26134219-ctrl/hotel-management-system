package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.model.Room;
import java.util.ArrayList;
import java.util.List;

// ReservationService is responsible for reservation logic
public class ReservationService { // 1. Excessive Responsibility
    private List<Room> availableRooms; // 2. Unnecessary Complexity

    public ReservationService() {
        this.availableRooms = new ArrayList<>(); // initialization
    }

    // set available rooms
    public void setAvailableRooms(List<Room> rooms) {
        this.availableRooms = rooms;
    }

    // handle reservation
    public boolean reserveRoom(Guest guest, Room room) {
        if (availableRooms.contains(room)) {
            // assume some reservation logic here
            System.out.println("Room reserved for " + guest.getName());
            return true;
        }
        return false;
    }

    // cancel reservation
    public boolean cancelReservation(Guest guest, Room room) {
        // assume some cancellation logic here
        System.out.println("Reservation canceled for " + guest.getName());
        return true;
    }

    // get all available rooms
    public List<Room> getAvailableRooms() {
        return availableRooms;
    }

    // get count of available rooms
    public int getAvailableRoomCount() {
        return availableRooms.size();
    }

    // check if room is available
    public boolean isRoomAvailable(Room room) {
        return availableRooms.contains(room);
    }

    // clear available rooms
    public void clearAvailableRooms() {
        availableRooms.clear();
    }
}