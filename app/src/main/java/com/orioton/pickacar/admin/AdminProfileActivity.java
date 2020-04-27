package com.orioton.pickacar.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.orioton.pickacar.R;

public class AdminProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);




        // action bar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
    }
}
