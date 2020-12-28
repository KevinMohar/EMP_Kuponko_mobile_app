package com.example.test2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.example.test2.Toolbox.Izdelek;
import com.example.test2.Toolbox.Racun;
import com.example.test2.Toolbox.Trgovina;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Racun> racuni = getRecipts();

        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HomeAdapter(racuni);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private ArrayList<Racun> getRecipts(){
        ArrayList<Racun> racuni = new ArrayList<>();

        // TODO: pridobi racune iz baze

        // --------------------------------samo za testeranje---------------------------------------
        racuni.add(new Racun(1, new Trgovina(1, "Mercator", "vehova"), Calendar.getInstance().getTime(), 300, new ArrayList<Izdelek>()));
        racuni.add(new Racun(2, new Trgovina(2, "Lidl", "asdasd"), Calendar.getInstance().getTime(), 240, new ArrayList<Izdelek>()));
        // -----------------------------------------------------------------------------------------

        return racuni;
    }
}
