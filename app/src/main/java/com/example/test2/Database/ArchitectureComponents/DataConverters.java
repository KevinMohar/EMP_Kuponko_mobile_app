package com.example.test2.Database.ArchitectureComponents;

import androidx.room.TypeConverter;

import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Izdelek;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<Izdelek> stringToIzdelki(String value) {
        Type listType = new TypeToken<List<Izdelek>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String izdelkiToString(List<Izdelek> izdelki) {
        Gson gson = new Gson();
        String json = gson.toJson(izdelki);
        return json;
    }
}
