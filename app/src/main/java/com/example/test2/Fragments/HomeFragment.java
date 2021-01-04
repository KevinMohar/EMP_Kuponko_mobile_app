package com.example.test2.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private View RootView;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addBtn;

    private KuponkoViewModel viewModel;

    private Mesec curreentMonth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_home, container, false);

        buildViewModel();

        getCurrentMonth();
        getRecipts();

        setTitleText();



        addBtn = RootView.findViewById(R.id.home_fragment_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipt();
            }
        });

        buildRecyclerView(RootView);

        return RootView;
    }

    private void getRecipts(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(curreentMonth.getDatum());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date from = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = cal.getTime();

        curreentMonth.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(from, to));
    }

    public void AddRecipt(){
        // TODO: odpres kamero --> slikas --> croppas --> dodas racun v bazo --> dodas racun v array


        // TODO: v tabeli meseci dodamo racun v seznam racunov trenutnega meseca


        // ostat more nakonc da updatas recycler view
        adapter.notifyItemInserted(curreentMonth.getRacuni().size()-1);
    }

    public void RemoveRecipt(int pos){
        // TODO: zbrisi racun iz seznma racunov trenutnega meseca
        viewModel.deleteRacun(curreentMonth.getRacuni().get(pos));
        curreentMonth.getRacuni().remove(pos);
        // da updejtas recycler view
        adapter.notifyItemRemoved(pos);
    }

    private void buildRecyclerView(final View view){
        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HomeAdapter();
        adapter.setRacuni(curreentMonth.getRacuni());

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

    private void buildViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
    }

    private void OpenRecipt(int pos){
        // TODO: odpremo pregled racuna

    }

    private void getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date datum = cal.getTime();

        curreentMonth = viewModel.getMonthByDate(datum);
        if(curreentMonth == null){
            curreentMonth = new Mesec(datum, 0);
            viewModel.insertMesec(curreentMonth);
        }

    }

    private void setTitleText(){
        TextView homeMesec = RootView.findViewById(R.id.home_mesec);
        homeMesec.setText(curreentMonth.getDisplayDate());

        TextView stroski = RootView.findViewById(R.id.home_stroski);
        stroski.setText("STROŠKI: " + curreentMonth.getStroski()+"€");
    }

}
