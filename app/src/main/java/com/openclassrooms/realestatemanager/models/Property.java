package com.openclassrooms.realestatemanager.models;

public class Property {

    private String city;
    private String type; // apartment, house, etc
    private int price;
    private int surface; // in meters
    private int mNumberOfRooms; // kitchen, living room, rooms, etc

    public Property(String city, int price){
        this.city = city;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
    public String getCity() {
        return city;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void setCity(String city) {
        this.city = city;
    }
}
