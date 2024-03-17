package com.example.tourapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingHistory {
    @SerializedName("historyBookings")
    private List<Booking> historyBookings;

    public BookingHistory(List<Booking> historyBookings) {
        this.historyBookings = historyBookings;
    }

    public List<Booking> getHistoryBookings() {
        return historyBookings;
    }

    public void setHistoryBookings(List<Booking> historyBookings) {
        this.historyBookings = historyBookings;
    }
}
