package com.example.test2.Toolbox;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Mesec {
    public int id;
    public Date datum;
    public float stroski;
    public ArrayList<Racun> racuni;

    public Mesec(int id){
        this.id = id;
        getInfoFromDatabase(id);
    }

    // Mesec(int id, Date mesec, float stroski, ArrayList<Racun> racuni)
    public Mesec(int id, Date datum, float stroski, ArrayList<Racun> racuni){
        this.id = id;
        this.datum = datum;
        this.stroski = stroski;
        this.racuni = racuni;
    }

    private void getInfoFromDatabase(int i){

    }

    public String getDate(){
        return  DateFormat.getDateTimeInstance().format(this.datum);
    }
}
