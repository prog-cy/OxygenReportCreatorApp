package com.example.firesbaseemlkit.pv_oxy.userdata;

public class AppUser{

    private String first_name;
    private String last_name;
    private String address;
    private String city;
    private String state;
    private String pin;
    private String email;
    private String mob_no;
    private String user_name;
    private String password;
    private String confirm_password;

    public AppUser(){

    }

    public AppUser(String first_name, String last_name, String address, String city, String state,
                   String pin, String email, String mob_no, String user_name, String password, String confirm_password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.email = email;
        this.mob_no = mob_no;
        this.user_name = user_name;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPin() {
        return pin;
    }

    public String getEmail() {
        return email;
    }

    public String getMob_no() {
        return mob_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }
}
