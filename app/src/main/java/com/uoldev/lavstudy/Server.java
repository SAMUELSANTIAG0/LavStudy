package com.uoldev.lavstudy;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.uoldev.lavstudy.Bean.RequestBean;
import com.uoldev.lavstudy.Dao.UserDao;

import java.util.HashMap;

/**
 * Created by Samuel Santiago on 15/04/2016.
 */
public class Server {

    private Firebase myFirebase;
    private UserDao userDao;
    private String statusAtual;
    private ChildEventListener statusListener;


    public Server(Context context) {
        Firebase.setAndroidContext(context);
        myFirebase = new Firebase("https://lavstudy.firebaseio.com/");
        userDao = new UserDao(context);
    }

    public void sendRequest(RequestBean requestBean) {
        requestBean.setStatus("Enviado");
        HashMap map = new HashMap<>();
        map.put("user", userDao.consult().getPersonName());
        map.put("service", requestBean.getService());
        map.put("status", requestBean.getStatus());
        map.put("date", requestBean.getDateRequest());
        map.put("latitude", requestBean.getParkingBean().getLatitude());
        map.put("longitude", requestBean.getParkingBean().getLongitude());
        map.put("dateParking", requestBean.getParkingBean().getDateString());

        myFirebase.child("requests").child(userDao.consult().getPersonId()).child(requestBean.getResquetNumber().toString()).setValue(map);
    }

    public void getStatus(RequestBean requestBean) {

        statusListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                statusAtual = dataSnapshot.child("status").getValue().toString();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        myFirebase.child("requests").child(userDao.consult().getPersonId()).child(requestBean.getResquetNumber().toString())
                .addChildEventListener(statusListener);

        requestBean.setStatus(statusAtual);
    }

    public void close() {
        if(statusListener != null) {
            myFirebase.removeEventListener(statusListener);
        }
        userDao.close();
    }

}
