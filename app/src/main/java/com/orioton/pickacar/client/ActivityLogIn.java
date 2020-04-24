package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.orioton.pickacar.R;

public class ActivityLogIn extends AppCompatActivity implements View.OnClickListener {

    // declaring the variables
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    TextView tvSignUpText;

    Button btnLogIn;
    ProgressBar progressBar;

    AwesomeValidation awesomeValidation;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // initializing the variables
        etEmail = findViewById(R.id.et_user_email);
        etPassword = findViewById(R.id.et_user_password);
        etConfirmPassword = findViewById(R.id.et_user_ps_confirm);

        btnLogIn = findViewById(R.id.btn_log_in);
        progressBar = findViewById(R.id.progressbar);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        // applying the validations
        awesomeValidation.addValidation(this, R.id.et_user_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.et_user_password,
                ".{8,}", R.string.invalid_password_length);

        awesomeValidation.addValidation(this, R.id.et_user_ps_confirm,
                R.id.et_user_password, R.string.invalid_password_confirm);

        mAuth = FirebaseAuth.getInstance();

        // setting up the signup text
        setSignUpText();

        btnLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_log_in):
                if (awesomeValidation.validate()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnLogIn.setVisibility(View.GONE);
                    // log the user in if the validation passed
                    String userEmail = etEmail.getText().toString().trim();
                    String userPass = etPassword.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                btnLogIn.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
                                // clearing all the top activities
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                btnLogIn.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    // validation failed
                    Toast.makeText(getApplicationContext(), "Please check for the errors", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void setSignUpText() {

        tvSignUpText = findViewById(R.id.user_signup_text);

        String loginText = "Don't have an account? Sign up here";

        SpannableString spannableString = new SpannableString(loginText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), ActivitySignUp.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 23, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUpText.setText(spannableString);
        tvSignUpText.setMovementMethod(LinkMovementMethod.getInstance());


    }

}
