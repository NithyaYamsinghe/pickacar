package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orioton.pickacar.R;

import java.util.HashMap;

public class AdminLoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =  100;
    GoogleSignInClient mGoogleSignInClient;

    // views
    TextInputEditText admin_edit_email_login, admin_edit_password_login;
    Button admin_login_button;
    SignInButton google_login_button;
    TextView admin_check_account_register, admin_reset_password;

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
        admin_reset_password = findViewById(R.id.admin_reset_password);
        google_login_button = findViewById(R.id.admin_login_google);

        progressDialog = new ProgressDialog(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




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
                finish();
            }
        });

        // recover password clicked

        admin_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRecoverPasswordDialog();

            }
        });

        // handle google login button click
        google_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // google sign in process
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }

    private void showRecoverPasswordDialog() {

        // alert dialog for reset password
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");


        // set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);

        // views
        final EditText emailEditText = new EditText(this);
        emailEditText.setHint("Enter your email");
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditText.setMinEms(16);
        linearLayout.addView(emailEditText);
        linearLayout.setPadding(10,10,10, 10);
        builder.setView(linearLayout);

        // buttons
        builder.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // insert  email

                String email = emailEditText.getText().toString().trim();
                BeginEmailRecovery(email);

            }
        });

        // buttons
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // show dialog
        builder.create().show();

    }

    private void BeginEmailRecovery(String email) {
        progressDialog.setMessage("Sending email...");

        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    Toast.makeText(AdminLoginActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AdminLoginActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(AdminLoginActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Logging...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();


                            // get user email and the uid from auth

                            String email = user.getEmail();
                            String uid = user.getUid();


                            // store user data

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("phone", "");
                            hashMap.put("image", "");

                            // firebase database

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("adminUsers");

                            // put data with in hash map
                            reference.child(uid).setValue(hashMap);










                            Toast.makeText(AdminLoginActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(AdminLoginActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();

                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AdminLoginActivity.this, "" + e.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
