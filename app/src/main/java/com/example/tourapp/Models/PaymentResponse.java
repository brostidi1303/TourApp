package com.example.tourapp.Models;

public class PaymentResponse {
    String vnpUrl;
    public PaymentResponse(String vnpUrl) {
        this.vnpUrl = vnpUrl;
    }

    public String getVnpUrl() {
        return vnpUrl;
    }

    public void setVnpUrl(String vnpUrl) {
        this.vnpUrl = vnpUrl;
    }
}
