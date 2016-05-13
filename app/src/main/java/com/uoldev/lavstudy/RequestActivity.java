package com.uoldev.lavstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.uoldev.lavstudy.Dao.ParkingDao;

public class RequestActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CheckBox checkBoxCar;
    private CheckBox checkBoxParking;
    private TextView textViewParkingDate;
    private CheckBox checkBoxService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Novo Pedido");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkBoxCar = (CheckBox)findViewById(R.id.checkBoxCar);
        checkBoxParking = (CheckBox)findViewById(R.id.checkBoxParking);
        textViewParkingDate = (TextView)findViewById(R.id.textViewParkingDate);
        checkBoxService = (CheckBox)findViewById(R.id.checkBoxService);

        if(!new ParkingDao(getApplicationContext()).isEmpy()){
            checkBoxParking.isChecked();
            textViewParkingDate.setText(new ParkingDao(getApplicationContext()).consultDate().toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.request, menu);
        return true;
    }

    public void goMap(View view){
    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

}
