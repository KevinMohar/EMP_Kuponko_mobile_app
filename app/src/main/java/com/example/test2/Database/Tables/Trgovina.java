package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trgovine")
public class Trgovina {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String ime;

    private String Naslov;

    public Trgovina(String ime, String naslov) {
        this.ime = ime;
        Naslov = naslov;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public String getNaslov() {
        return Naslov;
    }
}
