package com.example.test2.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.test2.Database.ArchitectureComponents.KuponkoDatabase;
import com.example.test2.Database.ArchitectureComponents.MesecDAO;
import com.example.test2.Database.ArchitectureComponents.RacunDAO;
import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;

public class RacunRepository {

    private RacunDAO racunDAO;

    public RacunRepository(Application application){
        KuponkoDatabase database = KuponkoDatabase.getInstance(application);
        racunDAO = database.racunDAO();
    }

    //------------------------------------------INSERTS---------------------------------------------
    public void Insert(Racun racun){
        new InsertRacunAsyncTask(racunDAO).execute(racun);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------UPDATES---------------------------------------------
    public void Update(Racun racun){
        new UpdateRacunAsyncTask(racunDAO).execute(racun);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------DELETES---------------------------------------------
    public void Delete(Racun racun){
        new DeleteRacunAsyncTask(racunDAO).execute(racun);
    }
    public void DeleteAllRacuns(){
        new DeleteAllRacunsAsyncTask(racunDAO).execute();
    }
    public void DeleteAllByMonth(Date from, Date to){
        new DeleteAllRacunsByMesecAsyncTask(racunDAO).execute(from, to);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------QUERIES---------------------------------------------
    public List<Racun> GetAllRacuns(){
        return racunDAO.GetAllRacuns();
    }
    public List<Racun> GetRacunByDate(Date from, Date to){
        return racunDAO.GetRacuniByMonth(from,to);
    }
    public Racun GetRacunById(int id){
        return racunDAO.GetRacunById(id);
    }
    //----------------------------------------------------------------------------------------------


    // AsyncTask je opuscen, zamenjaj s cim drugim
    //#####################################  INSERT TASKS  #########################################
    private static class InsertRacunAsyncTask extends AsyncTask<Racun, Void, Void>{
        private RacunDAO racunDAO;

        private InsertRacunAsyncTask(RacunDAO racunDAO){
            this.racunDAO = racunDAO;
        }

        @Override
        protected Void doInBackground(Racun... racuns) {
            racunDAO.Insert(racuns[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    private static class UpdateRacunAsyncTask extends AsyncTask<Racun, Void, Void>{
        private RacunDAO racunDAO;

        private UpdateRacunAsyncTask(RacunDAO racunDAO){
            this.racunDAO = racunDAO;
        }

        @Override
        protected Void doInBackground(Racun... racuns) {
            racunDAO.Update(racuns[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    private static class DeleteRacunAsyncTask extends AsyncTask<Racun, Void, Void>{
        private RacunDAO racunDAO;

        private DeleteRacunAsyncTask(RacunDAO racunDAO){
            this.racunDAO = racunDAO;
        }

        @Override
        protected Void doInBackground(Racun... racuns) {
            racunDAO.Delete(racuns[0]);
            return null;
        }
    }
    private static class DeleteAllRacunsAsyncTask extends AsyncTask<Void, Void, Void>{
        private RacunDAO racunDAO;

        private DeleteAllRacunsAsyncTask(RacunDAO racunDAO){
            this.racunDAO = racunDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            racunDAO.DeleteAllRacuns();
            return null;
        }
    }
    private static class DeleteAllRacunsByMesecAsyncTask extends AsyncTask<Date, Void, Void>{
        private RacunDAO racunDAO;

        private DeleteAllRacunsByMesecAsyncTask(RacunDAO racunDAO){
            this.racunDAO = racunDAO;
        }

        @Override
        protected Void doInBackground(Date... dates) {
            racunDAO.DeleteAllRacunsByMesec(dates[0], dates[1]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################

    //##############################################################################################
}
