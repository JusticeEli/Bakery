package com.justice.bakery;

public class Product {
    private String productName;
    private String image;
    private String shortDesc;
    private String fullDesc;
    private int price;
    private String category;

    public Product() {
    }

    public Product(String productName, String image, String shortDesc, String fullDesc, int price, String category) {
        this.productName = productName;
        this.image = image;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.price = price;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
