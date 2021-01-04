package com.example.test2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.test2.Fragments.ColorTestFragment;
import com.example.test2.Fragments.HomeFragment;
import com.example.test2.Fragments.OpenCVtestFragment;
import com.example.test2.Fragments.OverviewFragment;
import com.example.test2.Fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------- nastavitev side menuja-----------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
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

        // TODO: your code here
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
            case R.id.nav_nastavitve:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
                break;
            case R.id.nav_racun:
                Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();
                Login(this);
                break;
            // za testeranje openCV, odstrani na koncu
            case R.id.nav_OpenCV_test:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OpenCVtestFragment()).commit();
                break;
            case R.id.nav_Color_test:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ColorTestFragment()).commit();
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

    private void Login(Context c){
        // login v user acc
    }
}
