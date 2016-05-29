package com.uoldev.lavstudy;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.uoldev.lavstudy.Dao.UserDao;

public class Splash extends AppCompatActivity {

    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;
    private final int INTENT_RESQUIT = 100;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.hide();


        if(new UserDao(getApplicationContext()).isEmpy()) {
            //TODO: loginActivity
//            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        ImageView imageViewLoad = (ImageView)findViewById(R.id.imageViewLoad);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        animation.setDuration(SPLASH_TIME_OUT);
        imageViewLoad.startAnimation(animation);

        testGPS();
     }

    public void testGPS(){
        //É solicitado a ativação do GPS se ele não tiver ativo
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled){
            Toast.makeText(getApplicationContext(),"Favor habilitar o GPS!", Toast.LENGTH_SHORT).show();
            final Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(i, INTENT_RESQUIT);
                }
            }, 1000);
        }else {
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                @Override
                public void gotLocation(Location location){
                    MapsActivity.currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, locationResult);
            continueForMain();
        }
    }

    private void continueForMain(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_RESQUIT){
            if(resultCode == RESULT_OK){
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                    @Override
                    public void gotLocation(Location location){
                        MapsActivity.currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(this, locationResult);
                continueForMain();

            }else {
                Toast.makeText(getApplicationContext(),"Favor habilitar o GPS para continuar!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
