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

    @Ignore
    private Trgovina trgovina;

    private float znesek;

    private List<Izdelek> izdelki;

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

    public Trgovina getTrgovina(KuponkoViewModel viewModel) {
        if(trgovina == null)
            this.trgovina = viewModel.getTrgovinaById(idTrgovine);
        return trgovina;
    }

    public int getIdTrgovine() {
        return idTrgovine;
    }
}
