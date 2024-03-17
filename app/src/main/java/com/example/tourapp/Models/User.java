package com.example.tourapp.Models;

public class User {
    String _id;
    String fullName;
    String email;
    String password;
    String phone;

    public User(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String email){
        this.email = email;
    }

    public User(String fullName,String email,String phone){
        this.fullName = fullName;
        this.email = fullName;
        this.phone = phone;
    }


    public User(String _id, String fullName, String email, String password, String phone) {
        this._id = _id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
