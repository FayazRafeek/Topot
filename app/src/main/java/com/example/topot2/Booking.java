package com.example.topot2;

public class Booking {

    String bookingId;
    Product product;
    String time;
    String userId;

    public Booking(String bookingId, Product product, String time) {
        this.bookingId = bookingId;
        this.product = product;
        this.time = time;
    }

    public Booking() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
