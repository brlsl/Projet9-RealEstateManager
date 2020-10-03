package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;

import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PropertyContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long USER_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getPropertiesWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        //assertThat(cursor.moveToFirst(), is(true));
    }

    // ---

    private ContentValues generateProperty(){

        final ContentValues values = new ContentValues();

        values.put("agentId", "1");
        values.put("type", "House");
        values.put("city", "Paris");
        values.put("address", "1, avenue des Champs-Elysées");
        values.put("price", "1200000");
        values.put("surface", "120");
        values.put("numberOfRooms","5");
        values.put("numberOfBedrooms","2");
        values.put("numberOfBedrooms", "3");
        values.put("numberOfBathRooms", "3");
        values.put("description", "Description of the property...");
        values.put("pointsOfInterest", "Park, School, Hospital");
        values.put("dateAvailable", "01/09/2020");
        values.put("dateSold", "01/01/2020");
        values.put("agentNameSurname", "LSL Bruno");
        values.put("mainImagePath", "fakePath");
        values.put("isAvailable","false");

        return values;
    }

}
