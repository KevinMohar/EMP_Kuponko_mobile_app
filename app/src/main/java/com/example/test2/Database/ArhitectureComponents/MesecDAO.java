package com.example.test2.Database.ArhitectureComponents;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Mesec;

import java.util.Date;
import java.util.List;

@Dao
public interface MesecDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo

    @Insert
    void Insert(Mesec mesec);

    @Update
    void Update(Mesec mesec);

    @Delete
    void Delete(Mesec mesec);

    @Query("DELETE FROM meseci")
    void DeleteAllMonths();

    @Query("SELECT * FROM meseci")
    List<Mesec> GetAllMonths();

    @Query("SELECT * FROM meseci WHERE datum = :date")
    Mesec GetMonthByDate(Date date);


}
