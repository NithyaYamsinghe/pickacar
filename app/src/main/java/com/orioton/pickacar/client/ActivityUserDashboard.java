package com.orioton.pickacar.client;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.orioton.pickacar.R;

public class ActivityUserDashboard extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        toolbar = findViewById(R.id.toolbar_dash);
        setSupportActionBar(toolbar);

    }

    public void toPackages(View view) {
        Intent intent = new Intent(this, ActivityPackages.class);
        startActivity(intent);
    }
}
