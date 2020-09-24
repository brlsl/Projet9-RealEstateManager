package com.openclassrooms.realestatemanager.models;

import android.os.Parcel;
import android.os.Parcelable;

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

public class Property implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long agentId;
    private String type; // apartment, house, etc
    private String city;
    private String address;
    private int price;
    private int surface; // in square meters
    private int numberOfRooms; // kitchen, living room, rooms, etc
    private int numberOfBedrooms;
    private int numberOfBathRooms;
    private String description;
    private List<String> pointsOfInterest;
    private Date dateAvailable;
    private Date dateSold;
    private String agentNameSurname;
    private String mainImagePath;
    private boolean isAvailable;


    // constructor for database testing
    @Ignore
    public Property(long id, long agentId, String city, int price){
        this.id = id;
        this.agentId = agentId;
        this.city = city;
        this.price = price;
    }

    @Ignore
    public Property(long id, long agentId, int surface, int price){
        this.id = id;
        this.agentId = agentId;
        this.surface = surface;
        this.price = price;
    }

    @Ignore
    public Property(long id, long agentId, String type) {
        this.id = id;
        this.agentId = agentId;
        this.type = type;
    }


    public Property(long agentId, String city, String type, String address, int price, int surface,
                    int numberOfRooms, int numberOfBedrooms, int numberOfBathRooms, String description,
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

    @Ignore
    public Property(long id, long agentId, Date date) {
        this.id = id;
        this.agentId = agentId;
        this.dateAvailable = date;
    }


    protected Property(Parcel in) {
        id = in.readLong();
        agentId = in.readLong();
        type = in.readString();
        city = in.readString();
        address = in.readString();
        price = in.readInt();
        surface = in.readInt();
        numberOfRooms = in.readInt();
        numberOfBedrooms = in.readInt();
        numberOfBathRooms = in.readInt();
        description = in.readString();
        pointsOfInterest = in.createStringArrayList();
        agentNameSurname = in.readString();
        mainImagePath = in.readString();
        isAvailable = in.readByte() != 0;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(agentId);
        parcel.writeString(type);
        parcel.writeString(city);
        parcel.writeString(address);
        parcel.writeInt(price);
        parcel.writeInt(surface);
        parcel.writeInt(numberOfRooms);
        parcel.writeInt(numberOfBedrooms);
        parcel.writeInt(numberOfBathRooms);
        parcel.writeString(description);
        parcel.writeStringList(pointsOfInterest);
        parcel.writeString(agentNameSurname);
        parcel.writeString(mainImagePath);
        parcel.writeByte((byte) (isAvailable ? 1 : 0));
    }
}
