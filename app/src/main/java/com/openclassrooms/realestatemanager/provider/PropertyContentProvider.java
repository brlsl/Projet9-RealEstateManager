package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.models.Property;

import java.text.ParseException;

public class PropertyContentProvider extends ContentProvider {

    // FOR DATA
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.property_provider";
    public static final String TABLE_NAME_PROPERTY = "property_table";
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME_PROPERTY);

    @Override
    public boolean onCreate() { return true; }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null){
            long agentId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().getPropertyWithCursor(agentId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME_PROPERTY;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (getContext() != null){
            final long id;
            try {
                if (contentValues != null) {
                    id = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().createProperty(Property.fromContentValues(contentValues));
                    if (id != 0){
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, id);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    // we do not delete properties from database in this app
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null){
            final int count;
            try {
                if (contentValues != null) {
                    count = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().updateProperty(Property.fromContentValues(contentValues));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }

}