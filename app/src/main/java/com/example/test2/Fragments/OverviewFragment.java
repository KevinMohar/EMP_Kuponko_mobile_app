package com.example.test2.Fragments;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.Date;

public class OverviewFragment extends Fragment {

    private View RootView;

    private RecyclerView recyclerView;
    private OverviewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mesec> meseci;

    private KuponkoViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_pregled, container, false);

        buildViewModel();

        getMonths();

        buildRecyclerView(RootView);

        return RootView;
    }

    private void getMonths(){
        meseci = (ArrayList<Mesec>) viewModel.getAllMesec();
        if(meseci == null)
            meseci = new ArrayList<>();
        else{
            for(Mesec m : meseci){
                Calendar cal = Calendar.getInstance();
                cal.setTime(m.getDatum());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date from = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date to = cal.getTime();

                m.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(from, to));
            }
        }
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
                viewMonth(position);
            }

            @Override
            public void onDeleteClick(int position) {
                RemoveMesec(position);
            }
        });
    }

    private void viewMonth(int pos){
        MonthOverviewFragment fragment = new MonthOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("idMeseca", meseci.get(pos).getId());
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("tag")
                .commit();

    }

    public void RemoveMesec(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_warning, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_warning));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_warning_title))
                .setText(getResources().getString(R.string.alert_dialog_warning_title));
        ((TextView) view.findViewById(R.id.alert_dialog_warning_description))
                .setText(getResources().getString(R.string.alert_dialog_warning_description));
        ((Button) view.findViewById(R.id.alert_dialog_false_btn))
                .setText(getResources().getString(R.string.alert_dialog_btn_false));
        ((Button) view.findViewById(R.id.alert_dialog_true_btn))
                .setText(getResources().getString(R.string.alert_dialog_btn_true));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_false_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.alert_dialog_true_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mesec m = meseci.get(pos);
                Calendar cal = Calendar.getInstance();
                cal.setTime(m.getDatum());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date from = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date to = cal.getTime();
                viewModel.deleteAllRacunsByDate(from, to);

                viewModel.deleteMesec(m);
                // da updejtas recycler view
                meseci.remove(pos);
                adapter.notifyItemRemoved(pos);
                Toast.makeText(getContext(), "Podatki izbrisani", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }

    private void buildViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
    }
}
