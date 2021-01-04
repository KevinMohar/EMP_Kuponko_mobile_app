package com.example.test2.Fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Izdelek;
import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Racun> racuni;

    private FloatingActionButton addBtn;

    private KuponkoViewModel viewModel;

    private Mesec mesec;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
        mesec = getCurrentMonth();
        racuni = getRecipts();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(mesec.getDisplayDate());

        buildRecyclerView(view);

        addBtn = view.findViewById(R.id.home_fragment_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipt();
            }
        });


        // pusti na koncu
        return view;
    }

    private ArrayList<Racun> getRecipts(){

        //ArrayList<Racun> racuni = viewModel.getAllRacunsByMonth();
        ArrayList<Racun> racuni = new ArrayList<>();
        return racuni;
    }

    public void AddRecipt(){
        // TODO: odpres kamero --> slikas --> croppas --> dodas racun v bazo --> dodas racun v array


        // TODO: v tabeli meseci dodamo racun v seznam racunov trenutnega meseca


        // ostat more nakonc da updatas recycler view
        adapter.notifyItemInserted(racuni.size()-1);
    }

    public void RemoveRecipt(int pos){

        viewModel.deleteRacun(racuni.get(pos));
        // TODO: zbrisi racun iz seznma racunov trenutnega meseca

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

                // FIXME:-------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Izbran račun "+(position+1), Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }

            @Override
            public void onDeleteClick(int position) {
                RemoveRecipt(position);

                // FIXME:-------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Račun izbrisan", Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }
        });
    }

    private void OpenRecipt(int pos){
        // TODO: odpremo pregled racuna

    }

    private Mesec getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date datum = cal.getTime();
        Mesec m = viewModel.getMonthByDate(datum);

        if(m == null)
            return new Mesec(datum,0);
        return m;
    }

}
