package com.example.test2.Database.ArchitectureComponents;

import androidx.room.TypeConverter;

import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Izdelek;

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
    public static String izdelkiToString(List<Izdelek> izdelki) {
        StringBuilder sb = new StringBuilder();
        for(Izdelek i : izdelki){
            sb.append(i.toString()+";");
        }
        return sb.toString();
    }

    @TypeConverter
    public static List<Izdelek> stringToIzdelki(String izdelkiStr) {
        List<Izdelek> izdelki = new ArrayList<>();
        String[] tmp = izdelkiStr.split(";");
        for(String i : tmp){
            String[] a = i.split(",");
            izdelki.add(new Izdelek(a[0], Integer.parseInt(a[1]), Float.parseFloat(a[2])));
        }
        return izdelki;
    }
}
