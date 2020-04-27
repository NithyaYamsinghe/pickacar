package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;

public class AdminProfileActivity extends AppCompatActivity {

    // firebase auth
    FirebaseAuth firebaseAuth;

    // views
    TextView logged_in_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);




        // action bar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        // init views
        logged_in_user = findViewById(R.id.admin_profile_text);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void checkUserStatus(){

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){

            // set email of logged in user
            logged_in_user.setText(user.getEmail());

        }
        else{
            startActivity(new Intent(AdminProfileActivity.this, MainActivity.class));
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // inflate options menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_car_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();

        }
        else if (id == R.id.action_add){
            startActivity(new Intent(AdminProfileActivity.this, AddNewCarActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
