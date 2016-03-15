package com.uoldev.lavstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        bar.hide();
    }

    public void goLogon(View view){

        Intent intent= new Intent(LoginActivity.this, LogonActivity.class);
        startActivity(intent);
    }
}
