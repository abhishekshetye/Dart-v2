package com.codebreaker.dart.zomato;

/**
 * Created by abhishek on 2/19/17.
 */

public class Restaurant {

    String name;
    String rating;
    String address;
    String imageUrl;
    String cusines;
    String menuUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCusines() {
        return cusines;
    }

    public void setCusines(String cusines) {
        this.cusines = cusines;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
}
