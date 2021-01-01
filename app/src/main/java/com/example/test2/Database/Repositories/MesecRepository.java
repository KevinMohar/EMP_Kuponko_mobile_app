package com.example.test2.Database.Repositories;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.test2.Database.ArhitectureComponents.KuponkoDatabase;
import com.example.test2.Database.ArhitectureComponents.MesecDAO;
import com.example.test2.Database.ArhitectureComponents.RacunDAO;
import com.example.test2.Database.ArhitectureComponents.TrgovinaDAO;
import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;

import java.util.Date;
import java.util.List;

public class MesecRepository {

    private MesecDAO mesecDAO;

    private List<Mesec>allMonths;

    private KuponkoDatabase database;

    public MesecRepository(Application application){
        KuponkoDatabase database = KuponkoDatabase.getInstance(application);

        mesecDAO = database.mesecDAO();

        allMonths = mesecDAO.GetAllMonths();
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
        return allMonths;
    }
    public Mesec GetMonthByDate(Date date){
        return mesecDAO.GetMonthByDate(date);
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
