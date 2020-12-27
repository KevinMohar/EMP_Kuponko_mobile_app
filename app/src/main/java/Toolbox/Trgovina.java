package Toolbox;

public class Trgovina {

    public int ID;
    public String Ime;
    public String Naslov;

    public Trgovina(int id){
        this.ID = id;
        getDataFromDatabase();
    }

    public  Trgovina(int id, String ime, String naslov){
        this.ID = id;
        this.Ime = ime;
        this.Naslov = naslov;
    }

    private void getDataFromDatabase(){
        // poizvedba za v database;
    }

}
