package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.fragments.AdminHomeFragment;
import com.orioton.pickacar.admin.fragments.AdminProfileFragment;
import com.orioton.pickacar.admin.fragments.AdminUsersFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    // firebase auth
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        // action bar and title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");


        firebaseAuth = FirebaseAuth.getInstance();

        // bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav_admin);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);


        // default fragment
        actionBar.setTitle("Home");
        AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.content, adminHomeFragment, "");
        fragmentTransaction1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // handle item clicks
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    // home fragment
                    actionBar.setTitle("Home");
                    AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, adminHomeFragment, "");
                    fragmentTransaction1.commit();
                    return true;
                case R.id.bottom_nav_profile:
                    // profile fragment
                    actionBar.setTitle("Profile");
                    AdminProfileFragment adminProfileFragment = new AdminProfileFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, adminProfileFragment, "");
                    fragmentTransaction2.commit();
                    return true;


                case R.id.bottom_nav_users:
                    // users fragment
                    actionBar.setTitle("Users");
                    AdminUsersFragment adminUsersFragment = new AdminUsersFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, adminUsersFragment, "");
                    fragmentTransaction3.commit();

                    return true;


                case R.id.bottom_nav_cars:
                    // users fragment
                    actionBar.setTitle("Cars");
                    startActivity(new Intent(AdminDashboardActivity.this, AdminCarListActivity.class));

                    return true;

                case R.id.bottom_nav_packages:
                    // users fragment
                    actionBar.setTitle("Packages");
                    startActivity(new Intent(AdminDashboardActivity.this, AdminPackageListActivity.class));

                    return true;
            }
            return false;
        }
    };

    private void checkUserStatus() {

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            // set email of logged in user
//            logged_in_user.setText(user.getEmail());

        } else {
            startActivity(new Intent(AdminDashboardActivity.this, MainActivity.class));
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // inflate options menu


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.admin_car_list_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_logout) {
//            firebaseAuth.signOut();
//            checkUserStatus();
//
//        } else if (id == R.id.action_add) {
//            startActivity(new Intent(AdminDashboardActivity.this, AddNewCarActivity.class));
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


}
