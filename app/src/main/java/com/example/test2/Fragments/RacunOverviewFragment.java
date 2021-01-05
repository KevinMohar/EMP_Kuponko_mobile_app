package com.example.test2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Izdelek;
import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.example.test2.RecyclerView.RacunOverviewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RacunOverviewFragment extends Fragment {

    View RootView;
    private KuponkoViewModel viewModel;

    private FloatingActionButton addBtn;

    private RecyclerView recyclerView;
    private RacunOverviewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Racun racun;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.racun_overview, container, false);

        buildViewModel();

        GetData();
        SetRacunInfo();
        buildRecyclerView(RootView);
        addBtn = RootView.findViewById(R.id.home_fragment_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });

        return RootView;
    }

    private void buildViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
    }

    private void AddItem() {
        // TODO: odpre alert dialog za dodajanje izdelka --> doda izdelke v seznam izdelkov racuna --> updejta racun v bazi
    }

    private void buildRecyclerView(final View view) {
        recyclerView = view.findViewById(R.id.racun_overview_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new RacunOverviewAdapter();
        adapter.setIzdelki((ArrayList<Izdelek>) racun.getIzdelki());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RacunOverviewAdapter.OnItemClickListener() {
            @Override
            public void onCardClick(int position) {
                EditItem();
            }

            @Override
            public void onDeleteClick(int position) {
                DeleteItem();
            }
        });

    }

    private void DeleteItem() {
        // TODO: odpre alert dialog --> ce da --> izbrise izdelek iz seznama izdelkov racuna --> updejta racun v bazi
    }

    private void EditItem() {
        // TODO: odpre alert dialog kjer loh spreminjas podatke izdelka --> skupna cena se izracuna naknadno
    }

    private void SetRacunInfo(){
        // TODO: nastavi podatke v headerju (racun, datum, naslov, ...)
        TextView naziv = RootView.findViewById(R.id.racun_overview_title);
        TextView naslov = RootView.findViewById(R.id.racun_overview_naslov);
        TextView datum = RootView.findViewById(R.id.racun_overview_datum);
        TextView znesek = RootView.findViewById(R.id.racun_overview_znesek);
        TextView kolicina = RootView.findViewById(R.id.racun_overview_izdelki);

        naziv.setText("RAČUN "+racun.getId()+": "+racun.getTrgovina().getIme());
        naslov.setText(racun.getTrgovina().getNaslov());
        datum.setText(racun.getDatum()+"");
        znesek.setText(racun.getZnesek()+"€");
        kolicina.setText("Št izdelkov: "+racun.getIzdelki().size());
    }

    private void GetData(){
        // TODO: pridobi id racuna iz bundla in ga prebere iz baze
        if(getArguments() != null) {
            int id = getArguments().getInt("idRacuna");
            racun = viewModel.getRacunById(id);
        }
    }
}
