package com.example.test2.Database.ArchitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Racun;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface RacunDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo

    //#####################################  INSERT TASKS  #########################################
    @Insert
    void Insert(Racun racun);
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    @Update
    void Update(Racun racun);
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    @Delete
    void Delete(Racun racun);

    @Query("DELETE FROM racuni")
    void DeleteAllRacuns();

    @Query("DELETE FROM racuni WHERE datum BETWEEN :from AND :to")
    void DeleteAllRacunsByMesec(Date from, Date to);
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################
    @Query("SELECT * FROM racuni")
    List<Racun> GetAllRacuns();

    @Query("SELECT * FROM racuni WHERE id = :id")
    Racun GetRacunById(int id);

    @Query("SELECT * FROM racuni WHERE datum BETWEEN :from AND :to")
    List<Racun> GetRacuniByMonth(Date from, Date to);
    //##############################################################################################
}
