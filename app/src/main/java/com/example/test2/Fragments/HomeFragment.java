package com.example.test2.Fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.DBHelper;
import com.example.test2.Database.RacuniContract;
import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.example.test2.Toolbox.Izdelek;
import com.example.test2.Toolbox.Mesec;
import com.example.test2.Toolbox.Racun;
import com.example.test2.Toolbox.Trgovina;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Racun> racuni;

    private FloatingActionButton addBtn;

    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        racuni = getRecipts();

        buildRecyclerView(view);

        addBtn = view.findViewById(R.id.home_fragment_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipt();
            }
        });

        DBHelper dbHelper = new DBHelper(this.getContext());
        database = dbHelper.getWritableDatabase();


        // pusti na koncu
        return view;
    }

    private ArrayList<Racun> getRecipts(){
        ArrayList<Racun> racuni = new ArrayList<>();

        // TODO: pridobi racune iz baze in nafilaj array

        // --------------------------------samo za testeranje---------------------------------------
        racuni.add(new Racun(1, new Trgovina(1, "Mercator", "vehova"), Calendar.getInstance().getTime(), 300, new ArrayList<Izdelek>()));
        racuni.add(new Racun(2, new Trgovina(2, "Lidl", "asdasd"), Calendar.getInstance().getTime(), 240, new ArrayList<Izdelek>()));
        // -----------------------------------------------------------------------------------------

        return racuni;
    }

    public void AddRecipt(){
        // TODO: odpres kamero --> slikas --> croppas --> dodas racun v bazo --> dodas racun v array

        // samo placeholder, taprav racun bo genereran ko slikas neki oz k ga vneses rocno
        Racun racun = new Racun(1);

        // zapisemo racun v tabelo racun
        ContentValues cv = new ContentValues();
        cv.put(RacuniContract.RacunEntry.COLUM_1_NAME, racun.Trgovina.Ime);
        cv.put(RacuniContract.RacunEntry.COLUM_2_DATUM, racun.getDate());
        cv.put(RacuniContract.RacunEntry.COLUM_3_ZNESEK, racun.Znesek);
        database.insert(RacuniContract.RacunEntry.TABLE_NAME, null, cv);

        // TODO: v tabeli meseci dodamo racun v seznam racunov trenutnega meseca


        // --------------------------------samo za testeranje---------------------------------------
        racuni.add(new Racun(3, new Trgovina(2, "Hofer", "neki"), Calendar.getInstance().getTime(), 321, new ArrayList<Izdelek>()));
        // -----------------------------------------------------------------------------------------

        // ostat more nakonc da updatas recycler view
        adapter.notifyItemInserted(racuni.size()-1);
    }

    public void RemoveRecipt(int pos){

        // TODO: odpre se alert ce hoces zbrisat, ce da --> zbrises racun iz baze in iz seznama


        // da updejtas recycler view
        racuni.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    private void buildRecyclerView(final View view){
        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HomeAdapter(racuni);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OpenRecipt(position);

                //--------------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Izbran račun "+(position+1), Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }

            @Override
            public void onDeleteClick(int position) {
                RemoveRecipt(position);

                //--------------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Račun izbrisan", Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }
        });
    }

    private void OpenRecipt(int pos){
        // TODO: odpremo pregled racuna

    }

}
