package com.example.myfirstapp;

public class DataModel {

    String kenteken;
    String car_name;
    String website_link;

    public DataModel(String kenteken, String car_name, String website_link ) {
        this.kenteken=kenteken;
        this.car_name=car_name;
        this.website_link=website_link;
    }

    public String getKenteken() {
        return kenteken;
    }

    public String getCar_name() {
        return car_name;
    }

    public String getWebsite_link() {
        return website_link;
    }

}