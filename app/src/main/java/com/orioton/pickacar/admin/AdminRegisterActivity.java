package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orioton.pickacar.R;

import java.util.regex.Pattern;

public class AdminRegisterActivity extends AppCompatActivity {

    // views
    TextInputEditText admin_edit_email, admin_edit_password;
    Button admin_register_button;
    TextView admin_check_account;


    // progress bar to display while registering user
    ProgressDialog progressDialog;

    // declare an instance of firebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);


        // action bar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign up");


        // set up back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // init views

        admin_edit_email = findViewById(R.id.admin_edit_email);
        admin_edit_password = findViewById(R.id.admin_edit_Password);
        admin_register_button = findViewById(R.id.admin_register_action_button);
        admin_check_account = findViewById(R.id.admin_have_account_check_text);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering user...");

        // In the onCreate() method, initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();


        // handle register button clicks
        admin_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // input email, password
                String email = admin_edit_email.getText().toString().trim();
                String password = admin_edit_password.getText().toString().trim();

                // validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    //set error and focus to email edit text
                    admin_edit_email.setError("Invalid Email");
                    admin_edit_email.setFocusable(true);
                } else if (password.length() < 6) {

                    //set error and focus to password edit text
                    admin_edit_password.setError("Password length at least 6 characters");
                    admin_edit_password.setFocusable(true);

                } else {
                    registerUser(email, password);  // register the user
                }


            }
        });

        // handle text view  click listener
        admin_check_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
                finish();
            }
        });


    }

    private void registerUser(String email, String password) {
        // email and password valid show progress dialog to the user
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(AdminRegisterActivity.this, "Registered...\n " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminRegisterActivity.this, AdminProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Toast.makeText(AdminRegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // if something went wrong
                progressDialog.dismiss();
                Toast.makeText(AdminRegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go to previous activity
        return super.onSupportNavigateUp();
    }
}
