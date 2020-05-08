package com.justice.bakery;

public class Order {
    private String name;
    private String contact;
    private String email;
    private String address;
    private String product;
    private int cost;

    public Order() {
    }

    public Order(String name, String contact, String email, String address, String product, int cost) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.product = product;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
