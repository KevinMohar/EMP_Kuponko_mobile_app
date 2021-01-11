package com.example.test2.Fragments;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Izdelek;
import com.example.test2.R;
import com.example.test2.RecyclerView.RacunOverviewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        addBtn = RootView.findViewById(R.id.racun_overview_add_item_btn);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_izdelek, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_new_izdelek));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_izdelek_dodaj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText naziv = view.findViewById(R.id.alert_dialog_izdelek_naziv);
                EditText kolicina = view.findViewById(R.id.alert_dialog_izdelek_kolicna);
                EditText znesek = view.findViewById(R.id.alert_dialog_izdelek_cena);

                if(naziv.getText().toString().trim().length() != 0 && kolicina.getText().toString().trim().length() != 0 && znesek.getText().toString().trim().length() != 0){
                    int kol = Integer.parseInt(kolicina.getText().toString().trim());
                    float znes = Float.parseFloat(znesek.getText().toString().trim());
                    Izdelek i = new Izdelek(naziv.getText().toString().trim(), kol, znes);
                    racun.addIzdelek(i);
                    adapter.setIzdelki((ArrayList<Izdelek>) racun.getIzdelki());
                    adapter.notifyItemInserted(racun.getIzdelki().size()-1);
                    racun.nastaviZnesek(racun.getZnesek()+(i.kolicina*i.cena));
                    viewModel.deleteRacun(racun);
                    viewModel.insertRacun(racun);
                    SetRacunInfo();
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "Vpišite vsa polja", Toast.LENGTH_LONG);
                }
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
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
                EditItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                DeleteItem(position);
            }
        });

    }

    private void DeleteItem(int position) {
        racun.removeIzdelekAt(position);
        adapter.notifyItemRemoved(position);
    }

    private void EditItem(final int position) {
        // TODO: odpre alert dialog kjer loh spreminjas podatke izdelka --> skupna cena se izracuna naknadno
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_izdelek, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_new_izdelek));
        builder.setView(view);

        Izdelek izd = racun.getIzdelki().get(position);

        ((EditText)view.findViewById(R.id.alert_dialog_izdelek_naziv)).setText(izd.ime);
        ((EditText)view.findViewById(R.id.alert_dialog_izdelek_kolicna)).setText(izd.kolicina+"");
        ((EditText)view.findViewById(R.id.alert_dialog_izdelek_cena)).setText(izd.cena+"");

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_izdelek_dodaj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText naziv = view.findViewById(R.id.alert_dialog_izdelek_naziv);
                EditText kolicina = view.findViewById(R.id.alert_dialog_izdelek_kolicna);
                EditText znesek = view.findViewById(R.id.alert_dialog_izdelek_cena);

                if(naziv.getText().toString().trim().length() != 0 && kolicina.getText().toString().trim().length() != 0 && znesek.getText().toString().trim().length() != 0){
                    int kol = Integer.parseInt(kolicina.getText().toString().trim());
                    float znes = Float.parseFloat(znesek.getText().toString().trim());
                    Izdelek i = new Izdelek(naziv.getText().toString().trim(), kol, znes);
                    float cena = racun.getIzdelki().get(position).cena;
                    racun.replaceIzdelekAt(position, i);
                    adapter.setIzdelki((ArrayList<Izdelek>) racun.getIzdelki());
                    adapter.notifyItemChanged(position);
                    racun.nastaviZnesek(racun.getZnesek()+(i.kolicina*i.cena)-cena);
                    viewModel.updateRacun(racun);
                    SetRacunInfo();
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "Vpišite vsa polja", Toast.LENGTH_LONG);
                }
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void SetRacunInfo(){
        TextView naziv = RootView.findViewById(R.id.racun_overview_title);
        EditText naziv_ime = RootView.findViewById(R.id.racun_edit_title);
        EditText naslov = RootView.findViewById(R.id.racun_overview_naslov);
        TextView datum = RootView.findViewById(R.id.racun_overview_datum);
        TextView znesek = RootView.findViewById(R.id.racun_overview_znesek);
        TextView kolicina = RootView.findViewById(R.id.racun_overview_izdelki);

        naziv.setText("RAČUN "+racun.getId()+":");
        naziv_ime.setText(racun.getTrgovina().getIme());
        naslov.setText(racun.getTrgovina().getNaslov());
        datum.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(racun.getDatum()));
        znesek.setText(String.format("%.2f", racun.getZnesek()) +"€");
        kolicina.setText("Št izdelkov: "+racun.getIzdelki().size());

        naziv_ime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Trgovina trgovina = new Trgovina(s.toString(), racun.getTrgovina().getNaslov());
                viewModel.insertTrgovina(trgovina);
                Trgovina trgovinaNew = viewModel.getTrgovinaByNameAndAddress(trgovina.getIme(), trgovina.getNaslov());
                racun.setTrgovina(trgovinaNew);
                racun.setIdTrgovine(trgovinaNew.getId());
                viewModel.updateRacun(racun);
            }
        });

        naslov.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Trgovina trgovina = new Trgovina(racun.getTrgovina().getIme(), s.toString());
                viewModel.insertTrgovina(trgovina);
                Trgovina trgovinaNew = viewModel.getTrgovinaByNameAndAddress(trgovina.getIme(), trgovina.getNaslov());
                racun.setTrgovina(trgovinaNew);
                racun.setIdTrgovine(trgovinaNew.getId());
                viewModel.updateRacun(racun);
            }
        });
    }

    private void GetData(){
        if(getArguments() != null) {
            if(getArguments().containsKey("idRacuna")){
                int id = getArguments().getInt("idRacuna");
                racun = viewModel.getRacunById(id);
                Trgovina t = viewModel.getTrgovinaById(racun.getIdTrgovine());
                racun.setTrgovina(t);
                return;
            }
            String trgovina = getArguments().getString("racun_trgovina");
            String naslov = getArguments().getString("racun_naslov");

            Trgovina t = viewModel.getTrgovinaByNameAndAddress(trgovina, naslov);
            if(t == null){
                viewModel.insertTrgovina(new Trgovina(trgovina,naslov));
            }
            t = viewModel.getTrgovinaByNameAndAddress(trgovina, naslov);

            racun = new Racun(Calendar.getInstance().getTime(), t.getId(), 0, new ArrayList<Izdelek>());
            viewModel.insertRacun(racun);
            racun = viewModel.getRacunByDate(racun.getDatum());
            racun.setTrgovina(t);
        }
    }
}
