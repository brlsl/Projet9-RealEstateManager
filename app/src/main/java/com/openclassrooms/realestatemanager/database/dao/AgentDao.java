package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Agent;

import java.util.List;

@Dao
public interface AgentDao {

    @Query("SELECT * FROM agent_table")
    Cursor getAllAgent();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long createAgent(Agent agent);

    @Query("SELECT * FROM agent_table")
    LiveData<List<Agent>> getAgentList();

    @Query("SELECT * FROM agent_table WHERE id = :id")
    LiveData<Agent> getAgent(long id);

    @Update
    int updateAgent(Agent agent);

    @Delete
    void deleteAgent(Agent agent);

}
