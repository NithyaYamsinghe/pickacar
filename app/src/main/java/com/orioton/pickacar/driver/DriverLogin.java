package com.orioton.pickacar.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;

public class DriverLogin extends AppCompatActivity {

    private EditText email, password;
    private TextView link;
    private Button login;

   // private Firebase root;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();
      //  root = new Firebase("https://pickacar-b3bf4.firebaseio.com/drivers");

         email = (EditText) findViewById(R.id.et_driver_email);
        password = (EditText) findViewById(R.id.et_driver_password);
        login = (Button) findViewById(R.id.btn_driver_log_in);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                }
            }
        };

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

         //applying the validations
        awesomeValidation.addValidation(this, R.id.dr_et_driver_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.dr_et_driver_password,
                ".{8,}", R.string.invalid_password_length);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        // setting up the signup text
        setSignUpText();
    }

    private void setSignUpText() {

        link = findViewById(R.id.driver_signup_text);

        String loginText = "Don't have an account? Sign up here";

        SpannableString spannableString = new SpannableString(loginText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), DriverSignUp.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 23, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        link.setText(spannableString);
        link.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        String value1 = email.getText().toString().trim();
        String value2 = password.getText().toString().trim();

        if(TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)){
            Toast.makeText(getApplicationContext(), "Fields are empty...",Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(value1,value2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Sign In Problem",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Logged in successfully!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
