package com.orioton.pickacar.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orioton.pickacar.R;

public class AdminMainActivity extends AppCompatActivity {


    // views
    Button adminRegisterButton, adminSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // init views
        adminRegisterButton = findViewById(R.id.admin_register_button);
        adminSignInButton = findViewById(R.id.admin_sign_in_button);


        // handle register button click
        adminRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start register activity
                startActivity(new Intent(AdminMainActivity.this, AdminRegisterActivity.class));

            }
        });

        // handle login button click

        adminSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start login activity
                startActivity(new Intent(AdminMainActivity.this, AdminLoginActivity.class));

            }
        });
    }
}
