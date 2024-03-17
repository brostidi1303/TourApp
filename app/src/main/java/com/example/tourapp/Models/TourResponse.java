package com.example.tourapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourResponse {
    @SerializedName("tours")

    private List<Tour> tourList;

    public TourResponse(List<Tour> tourList){
        this.tourList =tourList;
    }

    public List<Tour> getTourList() {
        return tourList;
    }

    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
    }
}
