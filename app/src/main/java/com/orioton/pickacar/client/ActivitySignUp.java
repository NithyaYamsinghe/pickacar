package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.orioton.pickacar.R;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {

    // declaring the variables
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    TextView tvLoginText;

    Button btnSignUp;
    ProgressBar progressBar;

    AwesomeValidation awesomeValidation;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initializing the variables
        etEmail = findViewById(R.id.et_user_email);
        etPassword = findViewById(R.id.et_user_password);
        etConfirmPassword = findViewById(R.id.et_user_ps_confirm);

        btnSignUp = findViewById(R.id.btn_sign_up);
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

        // setting up the login text
        setLoginText();

        btnSignUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_sign_up):
                if (awesomeValidation.validate()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnSignUp.setVisibility(View.GONE);
                    // sign the user up if the validation passed
                    String userEmail = etEmail.getText().toString().trim();
                    String userPass = etPassword.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                btnSignUp.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "User has been registered!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                                startActivity(intent);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                btnSignUp.setVisibility(View.VISIBLE);
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

    private void setLoginText() {
        tvLoginText = findViewById(R.id.user_login_text);

        String loginText = "Already have an account? Login here";

        SpannableString spannableString = new SpannableString(loginText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvLoginText.setText(spannableString);
        tvLoginText.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
