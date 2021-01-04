package com.example.test2.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.test2.Database.ArchitectureComponents.KuponkoDatabase;
import com.example.test2.Database.ArchitectureComponents.MesecDAO;
import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Trgovina;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class MesecRepository {

    private MesecDAO mesecDAO;

    private KuponkoDatabase database;

    public MesecRepository(Application application){
        KuponkoDatabase database = KuponkoDatabase.getInstance(application);
        mesecDAO = database.mesecDAO();
    }

    //------------------------------------------INSERTS---------------------------------------------
    public void Insert(Mesec mesec){
        new InsertMesecAsyncTask(mesecDAO).execute(mesec);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------UPDATES---------------------------------------------
    public void Update(Mesec mesec){
        new UpdateMesecAsyncTask(mesecDAO).execute(mesec);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------DELETES---------------------------------------------
    public void Delete(Mesec mesec){
        new DeleteMesecAsyncTask(mesecDAO).execute(mesec);
    }
    public void DeleteAll(){
        new DeleteAllMesecAsyncTask(mesecDAO).execute();
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------QUERIES---------------------------------------------
    public List<Mesec> GetAllMonths(){
        return mesecDAO.GetAllMonths();
    }
    public Mesec GetMonthByDate(Date date){
        return mesecDAO.GetMonthByDate(date);
    }
    public Mesec GetMonthById(int id){
        return mesecDAO.GetMonthById(id);
    }
    //----------------------------------------------------------------------------------------------


    // AsyncTask je opuscen, zamenjaj s cim drugim
    //#####################################  INSERT TASKS  #########################################
    private static class InsertMesecAsyncTask extends AsyncTask<Mesec, Void, Void>{
        private MesecDAO mesecDAO;

        private InsertMesecAsyncTask(MesecDAO mesecDAO){
            this.mesecDAO = mesecDAO;
        }

        @Override
        protected Void doInBackground(Mesec... mesecs) {
            mesecDAO.Insert(mesecs[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    private static class UpdateMesecAsyncTask extends AsyncTask<Mesec, Void, Void>{
        private MesecDAO mesecDAO;

        private UpdateMesecAsyncTask(MesecDAO mesecDAO){
            this.mesecDAO = mesecDAO;
        }

        @Override
        protected Void doInBackground(Mesec... mesecs) {
            mesecDAO.Update(mesecs[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    private static class DeleteMesecAsyncTask extends AsyncTask<Mesec, Void, Void>{
        private MesecDAO mesecDAO;

        private DeleteMesecAsyncTask(MesecDAO mesecDAO){
            this.mesecDAO = mesecDAO;
        }

        @Override
        protected Void doInBackground(Mesec... mesecs) {
            mesecDAO.Delete(mesecs[0]);
            return null;
        }
    }
    private static class DeleteAllMesecAsyncTask extends AsyncTask<Void, Void, Void>{
        private MesecDAO mesecDAO;

        private DeleteAllMesecAsyncTask(MesecDAO mesecDAO){
            this.mesecDAO = mesecDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mesecDAO.DeleteAllMonths();
            return null;
        }
    }
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################

    //##############################################################################################
}
