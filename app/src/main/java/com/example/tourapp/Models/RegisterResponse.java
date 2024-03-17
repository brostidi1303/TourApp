package com.example.tourapp.Models;

public class RegisterResponse {
    private User user;
    private String token;

    public RegisterResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    // Định nghĩa lớp User bên trong UserResponse
    public static class User {
        private String _id;
        private String email;
        private String phone;

        public User(String _id, String email, String phone) {
            this._id = _id;
            this.email = email;
            this.phone = phone;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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
    }
}
