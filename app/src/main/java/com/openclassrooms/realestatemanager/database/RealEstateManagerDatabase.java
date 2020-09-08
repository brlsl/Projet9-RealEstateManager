package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.database.dao.ImageDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;


@Database(entities = {Agent.class, Property.class, Image.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RealEstateManagerDatabase extends RoomDatabase {
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract AgentDao agentDao();
    public abstract PropertyDao propertyDao();
    public abstract ImageDao imageDao();

    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "MyDatabase.db")
                            //.fallbackToDestructiveMigration()
                            //.addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues agent = new ContentValues();
                agent.put("id", 1);
                agent.put("name", "REM");
                agent.put("surname", "Agency");
                db.insert("agent_table", OnConflictStrategy.IGNORE, agent);

                ContentValues property = new ContentValues();
                property.put("agentId", 1);
                property.put("type", "Apartment");
                property.put("city", "CityTest");
                property.put("address","123 test street");
                property.put("price", "123");
                property.put("surface", "100");
                property.put("numberOfRooms", "6");
                property.put("numberOfBedrooms", "3");
                property.put("numberOfBathrooms", "1");
                property.put("description", "Nice apartment, well located");
                property.put("dateAvailable", "01/01/2021");
                property.put("pointsOfInterest", "School");
                property.put("agentNameSurname", "REM Agency");
                property.put("mainImagePath", "fakePath/");
                property.put("isAvailable",  true);

                db.insert("property_table", OnConflictStrategy.IGNORE, property);
            }
        };
    }

}
