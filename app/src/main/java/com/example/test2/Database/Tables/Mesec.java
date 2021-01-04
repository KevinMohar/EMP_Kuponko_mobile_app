package com.example.test2.Database.Tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.test2.Database.ViewModels.KuponkoViewModel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "meseci")
public class Mesec {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date datum;

    private float stroski;

    @Ignore
    private List<Racun> racuni;

    public Mesec(Date datum, float stroski) {
        this.datum = datum;
        this.stroski = stroski;
        this.racuni = racuni;
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

    public float getStroski() {
        return stroski;
    }

    public void getRacuni(KuponkoViewModel viewModel){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.datum);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = cal.getTime();
        cal.setTime(this.datum);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date from = cal.getTime();
        this.racuni = viewModel.getAllRacunsByMonth(from, to);
    }

    public String getDisplayDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.datum);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String dateForDisplay = "";

        switch (month){
            case 0:
                dateForDisplay+="Januar";
                break;
            case 1:
                dateForDisplay+="Februar";
                break;
            case 2:
                dateForDisplay+="Marec";
                break;
            case 3:
                dateForDisplay+="April";
                break;
            case 4:
                dateForDisplay+="Maj";
                break;
            case 5:
                dateForDisplay+="Junij";
                break;
            case 6:
                dateForDisplay+="Juli";
                break;
            case 7:
                dateForDisplay+="Avgust";
                break;
            case 8:
                dateForDisplay+="September";
                break;
            case 9:
                dateForDisplay+="Oktober";
                break;
            case 10:
                dateForDisplay+="November";
                break;
            case 11:
                dateForDisplay+="December";
                break;
        }

        dateForDisplay+=" "+year;
        return dateForDisplay;
    }
}
