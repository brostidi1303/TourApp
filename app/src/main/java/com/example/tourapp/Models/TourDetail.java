package com.example.tourapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourDetail {
    private Tour tour;

    public TourDetail(Tour tour) {
        this.tour = tour;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }
}


