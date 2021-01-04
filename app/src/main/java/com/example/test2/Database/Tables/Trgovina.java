package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trgovine")
public class Trgovina {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String ime;

    private String naslov;

    public Trgovina(String ime, String naslov) {
        this.ime = ime;
        this.naslov = naslov;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getNaslov() {
        return naslov;
    }
}
