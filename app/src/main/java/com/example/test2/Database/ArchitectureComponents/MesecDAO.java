package com.example.test2.Database.ArchitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test2.Database.Tables.Mesec;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MesecDAO {

    // V ta interface vstavis vse SQL poizvedbe k jih hoces met za to tabelo
    // nato mors dodat se calle na te poizvedbe v repository classih in nato se v ViewModel

    //#####################################  INSERT TASKS  #########################################
    @Insert
    void Insert(Mesec mesec);
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    @Update
    void Update(Mesec mesec);
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    @Delete
    void Delete(Mesec mesec);

    @Query("DELETE FROM meseci")
    void DeleteAllMonths();
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################
    @Query("SELECT * FROM meseci")
    List<Mesec> GetAllMonths();

    @Query("SELECT * FROM meseci WHERE datum = :date")
    Mesec GetMonthByDate(Date date);
    //##############################################################################################
}
