package com.hotel.model;

import java.util.Date;

public class Guest extends Person {
    private String guestId;
    private String roomNumber;
    private Date checkInDate;
    private Date checkOutDate;

    public Guest(String name, int age, String contactInfo, String guestId) {
        super(name, age, contactInfo);
        this.guestId = guestId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}