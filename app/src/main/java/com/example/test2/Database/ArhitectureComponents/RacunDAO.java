package com.example.test2.Database.ArhitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Racun;

import java.util.Date;
import java.util.List;

@Dao
public interface RacunDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo

    @Insert
    void Insert(Racun racun);

    @Update
    void Update(Racun racun);

    @Delete
    void Delete(Racun racun);

    @Query("DELETE FROM racuni")
    void DeleteAllRacuns();

    @Query("SELECT * FROM racuni")
    LiveData<List<Racun>> GetAllRacuns();

    @Query("SELECT * FROM racuni WHERE id = :id")
    Racun GetRacunById(int id);

    @Query("SELECT * FROM racuni WHERE datum = :date")
    LiveData<List<Racun>> GetRacuniByMonth(Date date);
}
