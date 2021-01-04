package com.example.test2.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.R;
import com.example.test2.RecyclerView.OverviewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OverviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private OverviewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mesec> meseci;

    private KuponkoViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregled, container, false);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);

        meseci = getMonths();

        buildRecyclerView(view);

        return view;
    }

    private ArrayList<Mesec> getMonths(){
        ArrayList<Mesec> meseci = (ArrayList<Mesec>) viewModel.getAllMesec();
        return meseci;
    }

    private void buildRecyclerView(final View view) {
        recyclerView = view.findViewById(R.id.pregled_fragment_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new OverviewAdapter(meseci);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OverviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                viewRecipt();

                // FIXME:-------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Izbran račun "+(position+1), Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }

            @Override
            public void onDeleteClick(int position) {
                RemoveMesec(position);


                // FIXME:-------------------------samo za testeranje----------------------------------------
                Toast.makeText(view.getContext(), "Podatki izbrisani", Toast.LENGTH_LONG).show();
                // -----------------------------------------------------------------------------------------
            }
        });
    }

    private void viewRecipt(){
        // TODO: odpre pregled racuna v novem fragmentu;
    }

    public void RemoveMesec(int pos){

        // TODO: odpre se alert ce hoces zbrisat, ce da --> zbrises mesec iz baze in iz seznama , zbrises tud vse racune od tega mesca


        // da updejtas recycler view
        meseci.remove(pos);
        adapter.notifyItemRemoved(pos);
    }
}
