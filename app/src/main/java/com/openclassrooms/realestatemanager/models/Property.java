package com.openclassrooms.realestatemanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "property_table",
        foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agentId"
        ))

public class Property implements Parcelable {

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


    // constructor for database testing
    @Ignore
    public Property(long agentId, String city, int price){
        this.agentId = agentId;
        this.city = city;
        this.price = price;
    }

    public Property(long agentId, String city, String type, int price, int surface, int numberOfRooms, int numberOfBedrooms, int numberOfBathRooms) {
        this.agentId = agentId;
        this.city = city;
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathRooms = numberOfBathRooms;
    }

    protected Property(Parcel in) {
        id = in.readLong();
        agentId = in.readLong();
        city = in.readString();
        type = in.readString();
        price = in.readInt();
        surface = in.readInt();
        numberOfRooms = in.readInt();
        numberOfBedrooms = in.readInt();
        numberOfBathRooms = in.readInt();
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(agentId);
        parcel.writeString(city);
        parcel.writeString(type);
        parcel.writeInt(price);
        parcel.writeInt(surface);
        parcel.writeInt(numberOfRooms);
        parcel.writeInt(numberOfBedrooms);
        parcel.writeInt(numberOfBathRooms);
    }
}
