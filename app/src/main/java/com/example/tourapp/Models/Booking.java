package com.example.tourapp.Models;

import java.util.Date;

public class Booking {
    private String tourId;
    private String userId;
    private String tourName;
    private String fullName;
    private String email;
    private String phone;
    private Date departureDate;
    private Date dateCreate;
    private int quantity;
    private int totalAmount;
    private String _id;
    private String paymentStatus;
    private String tourImage;

    // Constructors
    public Booking(){

    }

    public Booking(String tourId, String userId, String tourName, String fullName, String email, String phone, Date departureDate, Date dateCreate, int quantity, int totalAmount, String _id, String paymentStatus, String tourImage) {
        this.tourId = tourId;
        this.userId = userId;
        this.tourName = tourName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.departureDate = departureDate;
        this.dateCreate = dateCreate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this._id = _id;
        this.paymentStatus = paymentStatus;
        this.tourImage = tourImage;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTourImage() {
        return tourImage;
    }

    public void setTourImage(String tourImage) {
        this.tourImage = tourImage;
    }
}
