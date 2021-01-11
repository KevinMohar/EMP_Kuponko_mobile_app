package com.example.test2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Fragments.HomeFragment;
import com.example.test2.Fragments.NotifyFragment;
import com.example.test2.Fragments.OverviewFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    KuponkoViewModel viewModel;

    Mesec currentMonth;

    TextView navUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(KuponkoViewModel.class);
        getCurrentMonth();
        //-------------------------------- nastavitev side menuja-----------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.nav_header_stroski);
        navUsername.setText("STROŠKI TEKOČEGA MESECA: "+ String.format("%.2f", currentMonth.getStroski()) +"€");
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_domov);
        }
        //------------------------------------------------------------------------------------------


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_domov:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_pregled:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OverviewFragment())
                        .commit();
                break;
            case R.id.nav_notify:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NotifyFragment())
                        .commit();
                break;
            case R.id.nav_izbrisi_db:
                ask();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // on back button pressed
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
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

    private void ask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(this)
                .inflate(R.layout.alert_dialog_warning, (ConstraintLayout) this
                        .findViewById(R.id.alert_dialog_warning));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_warning_title))
                .setText("Izbriši vse podatke?");
        ((TextView) view.findViewById(R.id.alert_dialog_warning_description))
                .setText("Vsi podatki bodo izbrisani.");
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
                viewModel.deleteAllMesec();
                viewModel.deleteAllRacuns();
                viewModel.deleteAllTrgovina();
                Toast.makeText(view.getContext(), "Vsi podatki so bili izbrisani!", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}
