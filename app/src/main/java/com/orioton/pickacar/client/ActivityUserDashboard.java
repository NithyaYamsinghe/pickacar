package com.orioton.pickacar.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.orioton.pickacar.R;

public class ActivityUserDashboard extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        toolbar =  findViewById(R.id.tb_dashboard);
        setSupportActionBar(toolbar);
    }

}
