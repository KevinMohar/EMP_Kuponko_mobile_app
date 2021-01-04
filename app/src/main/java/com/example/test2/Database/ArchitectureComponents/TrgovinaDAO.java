package com.example.test2.Database.ArchitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Trgovina;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TrgovinaDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo
    // nato mors dodat se calle na te poizvedbe v repository classih in nato se v ViewModel

    //#####################################  INSERT TASKS  #########################################
    @Insert
    void Insert(Trgovina trgovina);
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    @Update
    void Update(Trgovina trgovina);
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    @Delete
    void Delete(Trgovina trgovina);

    @Query("DELETE FROM trgovine")
    void DeleteAllStores();
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################
    @Query("SELECT * FROM trgovine")
    Maybe<List<Trgovina>> GetAllStores();

    @Query("SELECT * FROM trgovine WHERE id = :id")
    Maybe<Trgovina> GetStoreById(int id);

    @Query("SELECT * FROM trgovine WHERE ime = :name")
    Maybe<List<Trgovina>> GetStoreByName(String name);

    @Query("SELECT * FROM trgovine WHERE ime = :name AND Naslov = :address")
    Maybe<Trgovina> GetStoreByNameAndAddress(String name, String address);
    //##############################################################################################
}
