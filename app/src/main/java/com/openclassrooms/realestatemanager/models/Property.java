package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "property_table",
        foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"
        ))

public class Property {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long agentId;

    private String city;
    private String type; // apartment, house, etc
    private int price;
    private int surface; // in square meters
    private int numberOfRooms; // kitchen, living room, rooms, etc
    private int numberOfBedrooms;
    private int numberOfBathRooms;

    @Ignore
    public Property(String city, int price){
        this.city = city;
        this.price = price;
    }

    public Property(long agentId, String city, int price){
        this.agentId = agentId;
        this.city = city;
        this.price = price;
    }

    public long getId() {
        return id;
    }
    public long getAgentId() {
        return agentId;
    }
    public int getPrice() {
        return price;
    }
    public String getCity() {
        return city;
    }


    public void setId(long id) {
        this.id = id;
    }
    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setCity(String city) {
        this.city = city;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathRooms() {
        return numberOfBathRooms;
    }

    public void setNumberOfBathRooms(int numberOfBathRooms) {
        this.numberOfBathRooms = numberOfBathRooms;
    }
}
