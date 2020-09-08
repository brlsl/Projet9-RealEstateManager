package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;


@Entity(tableName = "property_table",
        foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"
        ))

public class Property{

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long agentId;
    private String type; // apartment, house, etc
    private String city;
    private String address;
    private String price;
    private String surface; // in square meters
    private String numberOfRooms; // kitchen, living room, rooms, etc
    private String numberOfBedrooms;
    private String numberOfBathRooms;
    private String description;
    private List<String> pointsOfInterest;
    private Date dateAvailable;
    private Date dateSold;
    private String agentNameSurname;
    private String mainImagePath;
    private boolean isAvailable;


    // constructor for database testing
    @Ignore
    public Property(long id, long agentId, String city, String price){
        this.id = id;
        this.agentId = agentId;
        this.city = city;
        this.price = price;
    }

    public Property(long agentId, String city, String type, String address, String price, String surface,
                    String numberOfRooms, String numberOfBedrooms, String numberOfBathRooms, String description,
                    Date dateAvailable, Date dateSold, String agentNameSurname, List<String> pointsOfInterest, String mainImagePath, boolean isAvailable) {
        this.agentId = agentId;
        this.city = city;
        this.type = type;
        this.address = address;
        this.price = price;
        this.surface = surface;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathRooms = numberOfBathRooms;
        this.description = description;
        this.dateAvailable = dateAvailable;
        this.dateSold = dateSold;
        this.agentNameSurname = agentNameSurname;
        this.pointsOfInterest = pointsOfInterest;
        this.mainImagePath = mainImagePath;
        this.isAvailable = isAvailable;
    }


    // ----- GETTERS AND SETTERS -----
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getCity() {
        return city;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(String numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public String getNumberOfBathRooms() {
        return numberOfBathRooms;
    }

    public void setNumberOfBathRooms(String numberOfBathRooms) {
        this.numberOfBathRooms = numberOfBathRooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public Date getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(Date dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAgentNameSurname() {
        return agentNameSurname;
    }

    public void setAgentNameSurname(String agentNameSurname) {
        this.agentNameSurname = agentNameSurname;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }


    public Date getDateSold() {
        return dateSold;
    }

    public void setDateSold(Date dateSold) {
        this.dateSold = dateSold;
    }
}
