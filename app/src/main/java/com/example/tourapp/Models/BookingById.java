package com.example.tourapp.Models;

import com.google.gson.annotations.SerializedName;

public class BookingById {
    @SerializedName("booking")
    private Booking booking;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
