package com.example.test2.Fragments;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

    private Mesec currentMonth;

    private GraphView graphView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_home, container, false);

        buildViewModel();

        getCurrentMonth();
        getRecipts();

        setTitleText();

        graphView = RootView.findViewById(R.id.home_graph);
        GraphData();

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
        cal.setTime(currentMonth.getDatum());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date from = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = cal.getTime();

        currentMonth.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(from, to));
    }

    public void AddRecipt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_insert_racun, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_insert));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_insert_title))
                .setText(getResources().getString(R.string.alert_dialog_insert_title));
        ((Button) view.findViewById(R.id.alert_dialog_ročno_btn))
                .setText(getResources().getString(R.string.alert_dialog_insert_manual_btn));
        ((Button) view.findViewById(R.id.alert_dialog_slika_btn))
                .setText(getResources().getString(R.string.alert_dialog_insert_slika_btn));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_ročno_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VstaviRacunRocno();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.alert_dialog_slika_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VstaviRacunKotSliko();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

        // ostat more nakonc da updatas recycler view
        //adapter.notifyItemInserted(currentMonth.getRacuni().size()-1);
    }

    public void RemoveRecipt(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_warning, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_warning));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_warning_title))
                .setText(getResources().getString(R.string.alert_dialog_warning_title));
        ((TextView) view.findViewById(R.id.alert_dialog_warning_description))
                .setText(getResources().getString(R.string.alert_dialog_warning_description_racuni));
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
                viewModel.deleteRacun(currentMonth.getRacuni().get(pos));
                currentMonth.getRacuni().remove(pos);
                // da updejtas recycler view
                adapter.notifyItemRemoved(pos);
                Toast.makeText(view.getContext(), "Račun izbrisan", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void buildRecyclerView(final View view){
        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HomeAdapter();
        adapter.setRacuni(currentMonth.getRacuni());
        adapter.setViewModel(viewModel);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OpenRecipt(position);
            }

            @Override
            public void onDeleteClick(int position) {
                RemoveRecipt(position);
            }
        });
    }

    private void buildViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
    }

    private void OpenRecipt(int pos){
        // TODO: odpri pregled racuna
    }

    private void getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date datum = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        Date to = cal.getTime();
        currentMonth = viewModel.getMonthByDate(datum);
        if(currentMonth == null){
            currentMonth = new Mesec(datum, 0);
            currentMonth.setRacuni(new ArrayList<Racun>());
            viewModel.insertMesec(currentMonth);
        }else {
            //viewModel.deleteAllRacuns();
            //viewModel.deleteAllMesec();
            currentMonth.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(datum,to));
        }
    }

    private void setTitleText(){
        TextView homeMesec = RootView.findViewById(R.id.home_mesec);
        homeMesec.setText(currentMonth.getDisplayDate());

        TextView stroski = RootView.findViewById(R.id.home_stroski);
        stroski.setText("STROŠKI: " + String.format("%.2f", currentMonth.getStroski()) +"€");
    }

    private void GraphData(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth.getDatum());
        float stroski;
        for(int i = 1; i <=  cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            stroski = currentMonth.getStroskiByDay(i);
            if(stroski != 0)
                series.appendData(new DataPoint(i,stroski),true,31);
        }
        graphView.addSeries(series);
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(currentMonth.getLastDayOfMonth());
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(12);
        graphView.getViewport().setXAxisBoundsManual(true);
        GridLabelRenderer glr = graphView.getGridLabelRenderer();
        glr.setVerticalAxisTitle("Stroški €");
        glr.setHorizontalAxisTitle("Dan v mesecu");
        glr.setVerticalAxisTitleTextSize(45);
        glr.setPadding(80);
        glr.setNumHorizontalLabels(7);
    }

    private void VstaviRacunKotSliko(){
        // TODO: odpres kamero --> slikas --> croppas --> dodas racun v bazo --> dodas racun v array
        // TODO: ali odpres galerijo --> izberes fotko --> croppas --> dodas racun v bazo --> dodas racun v array
    }

    private void VstaviRacunRocno(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_new_racun, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_new_racun));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_new_racun_naprej).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText trgovina = view.findViewById(R.id.alert_dialog_new_racun_trgovina);
                EditText naslov = view.findViewById(R.id.alert_dialog_new_racun_naslov);

                if(trgovina.getText().toString().trim().length() != 0
                        && naslov.getText().toString().trim().length() != 0 ){

                    RacunOverviewFragment fragment = new RacunOverviewFragment();
                    Bundle args = new Bundle();
                    args.putString("racun_trgovina", trgovina.getText().toString().trim());
                    args.putString("racun_naslov", naslov.getText().toString().trim());
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("tag3")
                            .commit();
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

}
