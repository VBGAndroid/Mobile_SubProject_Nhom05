package com.example.mobile_subproject_nhom05.module;

public class Cart {
    private String key;
    private String name;
    private String image;
    private String price;
    private int quantity;
    private float totalPrice;

    public Cart(String key, String name, String image, String price, int quantity, float totalPrice) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Cart() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalQuantity) {
        this.totalPrice = totalQuantity;
    }
}
