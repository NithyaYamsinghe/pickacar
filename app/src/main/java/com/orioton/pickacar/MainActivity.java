package com.orioton.pickacar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orioton.pickacar.admin.ActivityAddCar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeAddCar(View view) {
        Intent intent = new Intent(this, ActivityAddCar.class);
        startActivity(intent);
    }
}
