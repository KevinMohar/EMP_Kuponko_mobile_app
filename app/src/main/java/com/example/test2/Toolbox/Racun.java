package com.example.test2.Toolbox;

import java.util.Date;
import java.util.List;

public class Racun {

    public int ID;
    public Trgovina Trgovina;
    public Date Datum;
    public float Znesek;

    public List<Izdelek> Izdelki;

    public Racun(int id){
        this.ID = id;
        getDataFromDatabase();
    }

    public Racun(int id, Trgovina trgovina, Date datum, float znesek, List<Izdelek> izdelki){
        this.ID = id;
        this.Trgovina = trgovina;
        this.Datum = datum;
        this.Znesek = znesek;
        this.Izdelki = izdelki;
    }

    private void getDataFromDatabase(){
        // poizvedba v db
    }

}
