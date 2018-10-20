package com.example.rodrigoespinoza.fragmentos.model;

public class Product {
    Integer id;
    String name;
    Integer stock;

    public Product(Integer id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}