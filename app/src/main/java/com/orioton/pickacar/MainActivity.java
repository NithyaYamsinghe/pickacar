package com.orioton.pickacar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

import com.orioton.pickacar.admin.AdminMainActivity;



import com.orioton.pickacar.admin.AdminMainActivity;
import com.orioton.pickacar.admin.AdminCarListActivity;

import com.orioton.pickacar.client.ActivityLogIn;
import com.orioton.pickacar.client.ActivitySignUp;
import com.orioton.pickacar.client.ActivityUserDashboard;
import com.orioton.pickacar.driver.DriverLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

//    public void changeAddCar(View view) {
//        Intent intent = new Intent(this, AddNewPackageActivity.class);
//        startActivity(intent);
//    }

    public void AdminSignUp(View view) {
        Intent intent = new Intent(this, AdminMainActivity.class);
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

    public void changeDriver(View view) {
        Intent intent = new Intent(this, DriverLogin.class);
        startActivity(intent);
    }
}
