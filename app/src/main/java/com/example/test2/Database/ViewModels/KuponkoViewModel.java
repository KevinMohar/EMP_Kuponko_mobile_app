package com.example.test2.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.test2.Database.Repositories.MesecRepository;
import com.example.test2.Database.Repositories.RacunRepository;
import com.example.test2.Database.Repositories.TrgovinaRepository;
import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;


public class KuponkoViewModel extends AndroidViewModel {
    private MesecRepository mesecRepository;
    private RacunRepository racunRepository;
    private TrgovinaRepository trgovinaRepository;


    public KuponkoViewModel(@NonNull Application application) {
        super(application);
        mesecRepository = new MesecRepository(application);
        racunRepository = new RacunRepository(application);
        trgovinaRepository = new TrgovinaRepository(application);
    }

    //#####################################  INSERT TASKS  #########################################
    public void insertRacun(Racun racun){ racunRepository.Insert(racun); }
    public void insertMesec(Mesec mesec){ mesecRepository.Insert(mesec); }
    public void insertTrgovina(Trgovina trgovina){trgovinaRepository.Insert(trgovina);}
    //##############################################################################################

    //#####################################  UPDATE TASKS  #########################################
    public void updateRacun(Racun racun){ racunRepository.Update(racun); }
    public void updateMesec(Mesec mesec){ mesecRepository.Update(mesec); }
    public void updateTrgovina(Trgovina trgovina){trgovinaRepository.Update(trgovina);}
    //##############################################################################################

    //#####################################  DELETE TASKS  #########################################
    public void deleteRacun(Racun racun){ racunRepository.Delete(racun);}
    public void deleteAllRacunsByDate(Date date){ racunRepository.DeleteAllByMonth(date);}
    public void deleteAllRacuns(){ racunRepository.DeleteAllRacuns();}
    public void deleteMesec(Mesec mesec){mesecRepository.Delete(mesec);}
    public void deleteAllMesec(){mesecRepository.DeleteAll();}
    public void deleteTrgovina(Trgovina trgovina){trgovinaRepository.Delete(trgovina);}
    //##############################################################################################

    //#####################################  QUERY TASKS  ##########################################
    public Maybe<List<Racun>> getAllRacuns(){return racunRepository.GetAllRacuns();}
    public Maybe<List<Racun>> getAllRacunsByMonth(Date from, Date to){return racunRepository.GetRacunByDate(from, to);}
    public Maybe<Racun> getRacunById(int id){return racunRepository.GetRacunById(id);}
    public Maybe<List<Mesec>> getAllMesec(){return mesecRepository.GetAllMonths();}
    public Maybe<Mesec> getMonthByDate(Date date){return mesecRepository.GetMonthByDate(date);}
    public Trgovina getTrgovinaById(int id){return trgovinaRepository.GetStoreById(id);}
    public List<Trgovina> getTrgovinaByName(String name){return trgovinaRepository.GetStoreByName(name);}
    public Trgovina getTrgovinaByNameAndAddress(String name, String address){
        return trgovinaRepository.GetStoreByNameAndAddress(name, address);
    }
    //##############################################################################################
}
