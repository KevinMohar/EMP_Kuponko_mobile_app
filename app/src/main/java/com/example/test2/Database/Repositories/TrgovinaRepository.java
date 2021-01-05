package com.example.test2.Database.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.test2.Database.ArchitectureComponents.KuponkoDatabase;
import com.example.test2.Database.ArchitectureComponents.TrgovinaDAO;
import com.example.test2.Database.Tables.Trgovina;

import java.util.List;

import io.reactivex.Maybe;

public class TrgovinaRepository {

    private TrgovinaDAO trgovinaDAO;

    public TrgovinaRepository(Application application){
        KuponkoDatabase database = KuponkoDatabase.getInstance(application);
        trgovinaDAO = database.trgovinaDAO();
    }

    //------------------------------------------INSERTS---------------------------------------------
    public void Insert(Trgovina trgovina){
       trgovinaDAO.Insert(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------UPDATES---------------------------------------------
    public void Update(Trgovina trgovina){
        trgovinaDAO.Update(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------DELETES---------------------------------------------
    public void Delete(Trgovina trgovina){
        new DeleteStoreAsyncTask(trgovinaDAO).execute(trgovina);
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------QUERIES---------------------------------------------
    public List<Trgovina> GetAllStores(){
        return trgovinaDAO.GetAllStores();
    }
    public Trgovina GetStoreByNameAndAddress(String name, String address){
        return trgovinaDAO.GetStoreByNameAndAddress(name,address);
    }
    public Trgovina GetStoreById(int id){
        return trgovinaDAO.GetStoreById(id);
    }
    public List<Trgovina> GetStoreByName(String name){ return trgovinaDAO.GetStoreByName(name);}
    //----------------------------------------------------------------------------------------------


    // AsyncTask je opuscen, zamenjaj s cim drugim
    //#####################################  INSERT TASKS  #########################################

    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################

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
