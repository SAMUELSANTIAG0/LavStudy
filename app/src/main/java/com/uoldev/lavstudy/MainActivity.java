package com.uoldev.lavstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;
import com.uoldev.lavstudy.Bean.RequestBean;
import com.uoldev.lavstudy.Dao.ParkingDao;
import com.uoldev.lavstudy.Dao.UserDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendMessegeFireData();
            }
        });

//        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            if(this.getClass() != MapsActivity.class){
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        } else if (id == R.id.nav_request) {
            startActivity(new Intent(getApplicationContext(), RequestActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit) {
            Bundle parametros = new Bundle();
            parametros.putBoolean("logout", true);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtras(parametros);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
           return true;
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (first) {
            ImageView imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhotoUser);
            TextView textViewNome = (TextView) findViewById(R.id.textViewNameUser);
            TextView textViewEmail = (TextView) findViewById(R.id.textViewEmailUser);
            UserDao userDao = new UserDao(MainActivity.this);
            textViewNome.setText(userDao.consult().getPersonName());
            textViewEmail.setText(userDao.consult().getPersonEmail().toString());
            Picasso.with(this).load(userDao.consult().getPersonPhoto()).resize(100, 110).into(imageViewPhoto);
            userDao.close();
        }
        first = false;
    }


    public void sendMessegeFireData(){
        if(new ParkingDao(getApplicationContext()).isEmpy()){
           startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }else {
            RequestBean requestBean = new RequestBean(getApplicationContext(), "Lavagem ecologica");
            Server server = new Server(getApplicationContext());
            server.sendRequest(requestBean);
            server.close();
        }
    }

}