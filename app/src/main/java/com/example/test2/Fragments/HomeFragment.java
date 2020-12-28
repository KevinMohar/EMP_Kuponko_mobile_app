package com.example.test2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test2.R;
import com.example.test2.Toolbox.Izdelek;
import com.example.test2.Toolbox.Racun;
import com.example.test2.Toolbox.Trgovina;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArrayList<Racun> racuni = getRecipts();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private ArrayList<Racun> getRecipts(){
        ArrayList<Racun> racuni = new ArrayList<>();

        // TODO: pridobi racune iz baze

        // samo za testeranje
        racuni.add(new Racun(1, new Trgovina(1), new Date(), 300, new ArrayList<Izdelek>()));

        return racuni;
    }
}
