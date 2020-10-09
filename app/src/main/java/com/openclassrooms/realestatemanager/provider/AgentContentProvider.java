package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.models.Agent;


public class AgentContentProvider extends ContentProvider {
    // FOR DATA
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.agent_provider";
    public static final String TABLE_NAME = "agent_table";
    public static final Uri URI_AGENT = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() { return true; }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null){
            final Cursor cursor = RealEstateManagerDatabase.getInstance(getContext()).agentDao().getAllAgent();
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (getContext() != null){
            final long id;
            if (contentValues != null) {
                id = RealEstateManagerDatabase.getInstance(getContext()).agentDao().createAgent(Agent.fromContentValues(contentValues));
                if (id != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, id);
                }
            }

        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    // we do not delete agent from database in this app
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null){
            final int count;
            if (contentValues != null) {
                count = RealEstateManagerDatabase.getInstance(getContext()).agentDao().updateAgent(Agent.fromContentValues(contentValues));
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }

}
