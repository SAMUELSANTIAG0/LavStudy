package com.uoldev.lavstudy;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.uoldev.lavstudy.Dao.UserDao;

public class Splash extends AppCompatActivity {

    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.hide();
        final Intent intent;


        if(new UserDao(getApplicationContext()).isEmpy()) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        ImageView imageViewLoad = (ImageView)findViewById(R.id.imageViewLoad);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animation.setDuration(SPLASH_TIME_OUT);
        imageViewLoad.startAnimation(animation);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                MapsActivity.currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);


     }
}
