package com.example.tourapp.Models;

import java.util.Date;

public class Pay {
    private String orderId;
    private String bookingId;
    private double amountTransaction;
    private Date createDate;
    private String paymentStatus;

    public Pay(String orderId, String bookingId, double amountTransaction, Date createDate, String paymentStatus) {
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.amountTransaction = amountTransaction;
        this.createDate = createDate;
        this.paymentStatus = paymentStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(double amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
