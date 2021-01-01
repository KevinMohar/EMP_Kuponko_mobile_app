package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.test2.Izdelek;

import java.util.Date;
import java.util.List;

@Entity(tableName = "racuni")
public class Racun {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date datum;

    private float znesek;

    private List<Izdelek> izdelki;

    public Racun(Date datum, float znesek, List<Izdelek> izdelki) {
        this.datum = datum;
        this.znesek = znesek;
        this.izdelki = izdelki;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public float getZnesek() {
        return znesek;
    }

    public List<Izdelek> getIzdelki() {
        return izdelki;
    }
}
