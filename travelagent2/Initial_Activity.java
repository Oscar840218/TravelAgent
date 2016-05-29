package com.example.oscar.travelagent2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Initial_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent().setClass(Initial_Activity.this, MainActivity.class));
            }
        }, 2000);
    }


}


