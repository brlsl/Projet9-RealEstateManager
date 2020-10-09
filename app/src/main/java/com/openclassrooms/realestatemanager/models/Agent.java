package com.openclassrooms.realestatemanager.models;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.ParseException;

@Entity(tableName = "agent_table")
public class Agent {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String surname;

    // for database testing
    @Ignore
    public Agent(long id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Agent(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public Agent() {

    }

    // GETTERS

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    // ------ UTILS ------
    public static Agent fromContentValues(ContentValues values) {
        final Agent agent = new Agent();
        if (values.containsKey("id")) agent.setId(values.getAsLong("id"));
        if (values.containsKey("name")) agent.setName(values.getAsString("name"));
        if (values.containsKey("surname")) agent.setSurname(values.getAsString("surname"));

        return agent;
    }

}