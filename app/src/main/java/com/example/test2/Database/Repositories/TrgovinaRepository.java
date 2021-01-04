package com.example.test2.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.example.test2.Database.ArchitectureComponents.KuponkoDatabase;
import com.example.test2.Database.ArchitectureComponents.MesecDAO;
import com.example.test2.Database.ArchitectureComponents.TrgovinaDAO;
import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Trgovina;

import java.util.Date;
import java.util.List;

public class TrgovinaRepository {

    private TrgovinaDAO trgovinaDAO;

    private List<Trgovina> allStores;

    private KuponkoDatabase database;

    public TrgovinaRepository(Application application){
        KuponkoDatabase database = KuponkoDatabase.getInstance(application);
        trgovinaDAO = database.trgovinaDAO();
        allStores = trgovinaDAO.GetAllStores();
    }

    //------------------------------------------INSERTS---------------------------------------------
    public void Insert(Trgovina trgovina){
        new InsertStoreAsyncTask(trgovinaDAO).execute(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------UPDATES---------------------------------------------
    public void Update(Trgovina trgovina){
        new UpdateStoreAsyncTask(trgovinaDAO).execute(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------DELETES---------------------------------------------
    public void Delete(Trgovina trgovina){
        new DeleteStoreAsyncTask(trgovinaDAO).execute(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------QUERIES---------------------------------------------
    public List<Trgovina> GetAllStores(){
        return allStores;
    }
    public Trgovina GetStoreByNameAndAddress(String name, String address){
        return trgovinaDAO.GetStoreByNameAndAddress(name,address);
    }
    public List<Trgovina> GetStoreByName(String name){ return trgovinaDAO.GetStoreByName(name);}
    //----------------------------------------------------------------------------------------------


    // AsyncTask je opuscen, zamenjaj s cim drugim
    //#####################################  INSERT TASKS  #########################################
    private static class InsertStoreAsyncTask extends AsyncTask<Trgovina, Void, Void>{
        private TrgovinaDAO trgovinaDAO;

        private InsertStoreAsyncTask(TrgovinaDAO trgovinaDAO){
            this.trgovinaDAO = trgovinaDAO;
        }

        @Override
        protected Void doInBackground(Trgovina... trgovinas) {
            trgovinaDAO.Insert(trgovinas[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    private static class UpdateStoreAsyncTask extends AsyncTask<Trgovina, Void, Void>{
        private TrgovinaDAO trgovinaDAO;

        private UpdateStoreAsyncTask(TrgovinaDAO trgovinaDAO){
            this.trgovinaDAO = trgovinaDAO;
        }

        @Override
        protected Void doInBackground(Trgovina... trgovinas) {
            trgovinaDAO.Update(trgovinas[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    private static class DeleteStoreAsyncTask extends AsyncTask<Trgovina, Void, Void>{
        private TrgovinaDAO trgovinaDAO;

        private DeleteStoreAsyncTask(TrgovinaDAO trgovinaDAO){
            this.trgovinaDAO = trgovinaDAO;
        }

        @Override
        protected Void doInBackground(Trgovina... trgovinas) {
            trgovinaDAO.Delete(trgovinas[0]);
            return null;
        }
    }
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################

    //##############################################################################################
}
