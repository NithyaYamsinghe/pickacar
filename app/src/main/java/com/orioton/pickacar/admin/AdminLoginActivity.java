package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

public class AdminLoginActivity extends AppCompatActivity {

    // views
    TextInputEditText admin_edit_email_login, admin_edit_password_login;
    Button admin_login_button;
    TextView admin_check_account_register;

    // declare an instance of firebaseAuth
    private FirebaseAuth firebaseAuth;

    // progress bar to display while registering user
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        // action bar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign in");


        // set up back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // init views
        admin_edit_email_login = findViewById(R.id.admin_login_edit_email);
        admin_edit_password_login = findViewById(R.id.admin_login_edit_Password);
        admin_login_button = findViewById(R.id.admin_login_action_button);
        admin_check_account_register = findViewById(R.id.admin_check_please_sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging user...");


        // In the onCreate() method, initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();

        // login button clicked
        admin_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // input data
                String email = admin_edit_email_login.getText().toString().trim();
                String password = admin_edit_password_login.getText().toString().trim();

                // validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    //set error and focus to email edit text
                    admin_edit_email_login.setError("Invalid Email");
                    admin_edit_email_login.setFocusable(true);
                } else if (password.length() < 6) {

                    //set error and focus to password edit text
                    admin_edit_password_login.setError("Password length at least 6 characters");
                    admin_edit_password_login.setFocusable(true);

                } else {
                    loginUser(email, password);  // register the user
                }


            }
        });

        // not have account clicked
        admin_check_account_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginActivity.this, AdminRegisterActivity.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(AdminLoginActivity.this, AdminProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(AdminLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // if something went wrong
                progressDialog.dismiss();
                Toast.makeText(AdminLoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go to previous activity
        return super.onSupportNavigateUp();
    }
}
