package com.example.mobile_subproject_nhom05.module;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Motor {

    @PrimaryKey
    @NonNull
    private String key;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "price")
    private String price;

    public Motor(String key, String name, String image, String price) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public Motor() {
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
}
