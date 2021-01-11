package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Izdelek;

import java.util.Date;
import java.util.List;

@Entity(tableName = "racuni")
public class Racun {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date datum;

    private int idTrgovine;

    private float znesek;

    private List<Izdelek> izdelki;

    @Ignore
    private Trgovina trgovina;

    public Racun(Date datum, int idTrgovine, float znesek, List<Izdelek> izdelki) {
        this.datum = datum;
        this.znesek = znesek;
        this.izdelki = izdelki;
        this.idTrgovine = idTrgovine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public Trgovina getTrgovina() {
        return this.trgovina;
    }

    public void setTrgovina(Trgovina t){
        this.trgovina = t;
    }

    public int getIdTrgovine() {
        return idTrgovine;
    }

    public void addIzdelek(Izdelek izde){ this.izdelki.add(izde);}

    public void nastaviZnesek(float newZnesek){ this.znesek = newZnesek;}

    public void removeIzdelekAt(int position){ this.izdelki.remove(position);}

    public void replaceIzdelekAt(int position, Izdelek izdelek){this.izdelki.set(position, izdelek);}

    public void setIdTrgovine(int idTrgovine) {this.idTrgovine = idTrgovine;}
}
