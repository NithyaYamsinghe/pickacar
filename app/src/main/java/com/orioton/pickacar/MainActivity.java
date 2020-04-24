package com.orioton.pickacar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orioton.pickacar.admin.AdminHomeActivity;
import com.orioton.pickacar.admin.CarListActivity;
import com.orioton.pickacar.client.ActivityLogIn;
import com.orioton.pickacar.client.ActivitySignUp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeAddCar(View view) {
        Intent intent = new Intent(this, CarListActivity.class);
        startActivity(intent);
    }

    public void changeSignUp(View view) {
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }

    public void changeLogIn(View view) {
        Intent intent = new Intent(this, ActivityLogIn.class);
        startActivity(intent);
    }
}
