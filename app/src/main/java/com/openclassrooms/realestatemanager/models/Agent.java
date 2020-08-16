package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "agent_table")
public class Agent {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String surname;

    public Agent(long id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
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

}