package com.orioton.pickacar.driver;

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
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.firebase.client.Firebase;
import com.orioton.pickacar.R;

public class DriverLogin extends AppCompatActivity {

    private EditText email, password;
    private TextView link;
    private Button login;

//    private Firebase root;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

      //  root = new Firebase("https://pickacar-b3bf4.firebaseio.com/drivers");

//        email = (EditText) findViewById(R.id.et_driver_email);
//        password = (EditText) findViewById(R.id.et_driver_password);
//        login = (Button) findViewById(R.id.btn_driver_log_in);
//
//         //applying the validations
//        awesomeValidation.addValidation(this, R.id.dr_et_driver_email,
//                Patterns.EMAIL_ADDRESS, R.string.invalid_email);
//
//        awesomeValidation.addValidation(this, R.id.dr_et_driver_password,
//                ".{8,}", R.string.invalid_password_length);

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
}
