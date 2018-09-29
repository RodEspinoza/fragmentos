package com.example.rodrigoespinoza.fragmentos.model;

import java.util.Date;

public class User {
    Integer id;
    String email;
    String pass;
    Date register_date;

    public User() {
    }

    public User(Integer id, String email, String pass, Date register_date) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.register_date = register_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }
}
