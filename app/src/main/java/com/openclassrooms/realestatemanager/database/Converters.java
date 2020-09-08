package com.openclassrooms.realestatemanager.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> restoreList(String listOfString) {
        return new Gson().fromJson(listOfString, new TypeToken<List<String>>() {}.getType());
    }

    @TypeConverter
    public static String saveList(List<String> listOfString) {
        return new Gson().toJson(listOfString);
    }
}
