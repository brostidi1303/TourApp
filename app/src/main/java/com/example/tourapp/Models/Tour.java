package com.example.tourapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Tour {
    String _id, tourName, address, imagePath, description;
    Date departureDate;
    Number price, size;
    Boolean feature;

    public Tour(String _id, String tourName, String address, String imagePath, String description, Date departureDate, Number price, Number size, Boolean feature) {
        this._id = _id;
        this.tourName = tourName;
        this.address = address;
        this.imagePath = imagePath;
        this.description = description;
        this.departureDate = departureDate;
        this.price = price;
        this.size = size;
        this.feature = feature;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public Number getSize() {
        return size;
    }

    public void setSize(Number size) {
        this.size = size;
    }

    public Boolean getFeature() {
        return feature;
    }

    public void setFeature(Boolean feature) {
        this.feature = feature;
    }
}
