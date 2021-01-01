package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "meseci")
public class Mesec {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date datum;

    private float stroski;

    private List<Racun> racuni;        // id-ji racunov

    public Mesec(Date datum, float stroski, List<Racun> racuni) {
        this.datum = datum;
        this.stroski = stroski;
        this.racuni = racuni;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public float getStroski() {
        return stroski;
    }

    public List<Racun> getRacuni() {
        return racuni;
    }

    public String getDateString(){
        return datum.toString();
    }
}
