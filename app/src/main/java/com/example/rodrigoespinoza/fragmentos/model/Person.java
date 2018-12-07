package com.example.rodrigoespinoza.fragmentos.model;

public class Person {
    String id;
    String rut;
    String name;
    String last_name;
    String sexo;
    String location;
    String id_user;

    public Person() {

    }

    public Person(String id, String rut, String name, String last_name, String sexo, String location, String id_user) {
        this.id = id;
        this.rut = rut;
        this.name = name;
        this.last_name = last_name;
        this.sexo = sexo;
        this.location = location;
        this.id_user = id_user;
    }

    public Person(String id_user){
        this.id_user = id_user;
    }

    public Person(String name, String last_name, String sexo){
        this.name = name;
        this.last_name = last_name;
        this.sexo = sexo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
