package com.example.test2.Database.ArchitectureComponents;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;

@Database(entities = {Trgovina.class, Racun.class, Mesec.class}, version = 1)
public abstract class KuponkoDatabase extends RoomDatabase {

    private static KuponkoDatabase instance;

    // ----------------------------Vstavi DAO objekte za vse classe---------------------------------

    public abstract TrgovinaDAO trgovinaDAO();

    public abstract MesecDAO mesecDAO();

    public abstract RacunDAO racunDAO();

    // ---------------------------------------------------------------------------------------------

    public static synchronized KuponkoDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    KuponkoDatabase.class, "Kuponko_database")
                    .fallbackToDestructiveMigration()               // deletes db on version update
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private TrgovinaDAO trgovinaDAO;
        private MesecDAO mesecDAO;
        private RacunDAO racunDAO;

        private PopulateDbAsyncTask(KuponkoDatabase db){
            trgovinaDAO = db.trgovinaDAO();
            mesecDAO = db.mesecDAO();
            racunDAO = db.racunDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            trgovinaDAO.Insert(new Trgovina("Mercator", "Zaloška cesta 168, 1260 Ljubljana - Polje"));
            trgovinaDAO.Insert(new Trgovina("Lidl", "Toplarniška ulica 10, 1000 Ljubljana"));
            trgovinaDAO.Insert(new Trgovina("Hofer", "Chengdujska cesta 1, 1000 Ljubljana"));
            return null;
        }
    }
}
