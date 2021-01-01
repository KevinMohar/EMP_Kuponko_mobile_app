package com.example.test2.Database.ArhitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Trgovina;

import java.util.List;

@Dao
public interface TrgovinaDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo

    @Insert
    void Insert(Trgovina trgovina);

    @Update
    void Update(Trgovina trgovina);

    @Delete
    void Delete(Trgovina trgovina);

    @Query("DELETE FROM trgovine")
    void DeleteAllStores();

    @Query("SELECT * FROM trgovine")
    LiveData<List<Trgovina>> getAllStores();

    @Query("SELECT * FROM trgovine WHERE ime = :name AND Naslov = :address")
    List<Trgovina> getStoreByName(String name, String address);
}
