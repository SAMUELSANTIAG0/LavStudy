package com.uoldev.lavstudy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uoldev.lavstudy.Dao.ParkingDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends MainActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int ZOOM = 18;
    public static LatLng currentLocation;
    private LatLng lastParking;
    private Date lastParkingDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Polyline polyline;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //É solicitado a ativação do GPS se ele não tiver ativo
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

    }

    public void upLoadCurrentLocation() {

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMapLongClickListener(mapClick);

        upLoadCurrentLocation();


        ParkingDao parkingDao = new ParkingDao(getApplicationContext());
        if (parkingDao.isEmpy()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CameraPosition cameraPosition = CameraPosition.builder().target(currentLocation).zoom(ZOOM).bearing(360).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
                }
            }, 500);
        } else {
            lastParking = parkingDao.consult();
            lastParkingDate = parkingDao.consultDate();
            CameraPosition cameraPosition = CameraPosition.builder().target(lastParking).zoom(ZOOM).bearing(360).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(lastParking);
            markerOptions.title("Estacionado em " + dateFormat.format(lastParkingDate));
            mMap.addMarker(markerOptions);
        }
        parkingDao.close();
        mHandler = new Handler();
    }

    private GoogleMap.OnMapLongClickListener mapClick = new GoogleMap.OnMapLongClickListener() {

        @Override
        public void onMapLongClick(LatLng point) {
            if (mMap != null) {
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                new ParkingDao(getApplicationContext()).parked(markerOptions.getPosition());
                lastParkingDate = new ParkingDao(getApplicationContext()).consultDate();
                markerOptions.title("Estacionado em " + dateFormat.format(lastParkingDate));
                mMap.addMarker(markerOptions);
            }
        }
    };


    public void mapRoute(View view) {
        boolean tracarRota = true;
        Button button = (Button)findViewById(R.id.buttonRouta);

        if(tracarRota) {
            if (!new ParkingDao(getApplicationContext()).isEmpy()) {
                upLoadCurrentLocation();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(new ParkingDao(getApplicationContext()).consult());
                polylineOptions.add(currentLocation);
                polyline = mMap.addPolyline(polylineOptions);
                CameraPosition cameraPosition = CameraPosition.builder().target(currentLocation).zoom(ZOOM).bearing(360).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
                mStatusChecker.run();
                tracarRota = false;
                button.setText("Cancelar rota");
            }
        }else {
            mHandler.removeCallbacks(mStatusChecker);
            tracarRota = true;
            button.setText("Traçar rota");
        }

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                upLoadPolyline(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 3000);
            }
        }
    };


    public void upLoadPolyline() {

        upLoadCurrentLocation();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(currentLocation);
        latLngs.add(new ParkingDao(getApplicationContext()).consult());
        polyline.setPoints(latLngs);

    }

    public void checkIn(View view) {
        Button check = (Button) findViewById(R.id.buttonCheck);
        if (new ParkingDao(getApplicationContext()).isEmpy()) {
            check.setText("Desmarcar");
            mMap.clear();
            upLoadCurrentLocation();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            new ParkingDao(getApplicationContext()).parked(currentLocation);
            lastParkingDate = new ParkingDao(getApplicationContext()).consultDate();
            markerOptions.title("Estacionado em " + dateFormat.format(lastParkingDate));
            mMap.addMarker(markerOptions);
        } else {
            check.setText("Marcar");
            mapClear();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void mapClear() {
        mMap.clear();
        new ParkingDao(getApplicationContext()).reset();
    }
}
