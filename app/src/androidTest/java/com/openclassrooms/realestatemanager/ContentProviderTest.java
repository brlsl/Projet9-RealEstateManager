package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.room.Room;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;

import com.openclassrooms.realestatemanager.provider.AgentContentProvider;
import com.openclassrooms.realestatemanager.provider.ImageContentProvider;
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;
    private Context mContext;

    // DATA SET FOR TEST
    private static long AGENT_ID = (long) (Math.random() * 100000);
    private static long PROPERTY_ID = (long) (Math.random() * 100000);
    private static String AGENT_NAME = "REAL ESTATE";
    private static String AGENT_SURNAME = "TEST";

    private List<String> pathList = Arrays.asList("storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/hall.jpg",
            "storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/living_room.jpg",
            "storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/kitchen.jpg",
            "storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/bedroom.jpg",
            "storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/bathroom.jpg");

    @Before
    public void setUp() {
        mContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Room.inMemoryDatabaseBuilder(mContext,
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = mContext.getContentResolver();

    }

    @Test
    public void getProperties() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        if (cursor.getCount() == 0){
            assertThat(cursor.getCount(), is(0));
        }
        cursor.close();
    }

    @Test
    public void insertDataFromDatabase(){
        // before adding agent
        final Cursor cursorAgentBefore = mContentResolver.query(ContentUris.withAppendedId(AgentContentProvider.URI_AGENT, AGENT_ID), null,null,null, null);
        assertThat(cursorAgentBefore, notNullValue());
        // add agent
        mContentResolver.insert(AgentContentProvider.URI_AGENT, generateAgent());
        // TEST after adding agent
        final Cursor cursorAgentAfter = mContentResolver.query(ContentUris.withAppendedId(AgentContentProvider.URI_AGENT, AGENT_ID), null,null,null, null);
        assertThat(cursorAgentAfter, notNullValue());
        assertThat(cursorAgentAfter.getCount(), is(cursorAgentBefore.getCount()+1));
        // before adding property
        final Cursor cursorPropertyBefore = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, AGENT_ID), null, null, null, null);
        assertThat(cursorPropertyBefore, notNullValue());
        //  add property
        mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty());
        // TEST after adding property
        final Cursor cursor2 = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, AGENT_ID), null, null, null, null);
        assertThat(cursor2, notNullValue());
        assertThat(cursor2.getCount(), is(cursorPropertyBefore.getCount()+1));
        // before adding image
        final Cursor cursorImageBefore = mContentResolver.query(ContentUris.withAppendedId(ImageContentProvider.URI_IMAGE, PROPERTY_ID), null,null,null, null);
        // add images path from a list
        for (int i = 0; i < pathList.size() ; i++) {
            mContentResolver.insert(ImageContentProvider.URI_IMAGE, generateImages(i));
        }
        assertThat(cursorImageBefore, notNullValue());
        assertThat(cursorImageBefore.getCount(), is(0));

        // TEST
        final Cursor cursorImageAfter = mContentResolver.query(ContentUris.withAppendedId(ImageContentProvider.URI_IMAGE, PROPERTY_ID), null,null,null, null);
        assertThat(cursorImageAfter, notNullValue());
        assertThat(cursorImageAfter.getCount(), is (cursorImageBefore.getCount()+ pathList.size()));
    }

    private ContentValues generateAgent() {
        final ContentValues values = new ContentValues();
        values.put("id", AGENT_ID);
        values.put("name", AGENT_NAME);
        values.put("surname", AGENT_SURNAME);
        return values;
    }

    private ContentValues generateProperty(){
        final ContentValues values = new ContentValues();

        String mainImagePath = "storage/emulated/0/Android/data/com.openclassrooms.realestatemanager/files/Pictures/living_room.jpg";

        values.put("id", PROPERTY_ID);
        values.put("agentId", AGENT_ID);
        values.put("type", "House");
        values.put("city", "Paris");
        values.put("address", "1, avenue des Champs-Elysées");
        values.put("price", "9999");
        values.put("currency", "€");
        values.put("surface", "120");
        values.put("numberOfRooms","5");
        values.put("numberOfBedrooms","2");
        values.put("numberOfBedrooms", "2");
        values.put("numberOfBathRooms", "2");
        values.put("description", "Description of the property...");
        values.put("pointsOfInterest", "Park, School, Hospital");
        values.put("dateAvailable", "01/09/2020");
        values.put("dateSold", "01/01/2020");
        values.put("agentNameSurname", AGENT_NAME +" "+ AGENT_SURNAME);
        values.put("mainImagePath", mainImagePath);
        values.put("isAvailable","false");

        return values;
    }


    private ContentValues generateImages(int i) {
        final ContentValues values = new ContentValues();
        values.put("id", i+1); // id cannot be 0;
        values.put("propertyId", PROPERTY_ID);
        values.put("imagePath", pathList.get(i));
        values.put("imageTitle", "Room " + (i+1));

        return values;
    }

}
