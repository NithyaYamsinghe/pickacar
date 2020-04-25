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
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.orioton.pickacar.R;
import com.orioton.pickacar.driver.model.DriverModel;

public class DriverSignUp extends AppCompatActivity {

    private EditText email,phone,password,confirm;
    private TextView link;
    private Button signup;

    private Firebase root;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);

        root = new Firebase("https://pickacar-b3bf4.firebaseio.com/drivers");

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        email = (EditText) findViewById(R.id.dr_et_driver_email);
        phone = (EditText) findViewById(R.id.dr_et_driver_phone);
        password = (EditText) findViewById(R.id.dr_et_driver_password);
        confirm = (EditText) findViewById(R.id.dr_et_driver_ps_confirm);
        signup = (Button) findViewById(R.id.dr_btn_driver_sign_up);



        // applying the validations
        awesomeValidation.addValidation(this, R.id.dr_et_driver_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.dr_et_driver_phone,
                ".{10,}", R.string.invalid_phone);

        awesomeValidation.addValidation(this, R.id.dr_et_driver_password,
                ".{8,}", R.string.invalid_password_length);

        awesomeValidation.addValidation(this, R.id.dr_et_driver_ps_confirm,
                R.id.dr_et_driver_password, R.string.invalid_password_confirm);

        // setting up the login text
        setLoginText();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {


                    String value1 = email.getText().toString().trim();
                    String value2 = phone.getText().toString().trim();
                    String value3 = password.getText().toString().trim();

                    DriverModel driver = new DriverModel(value1,value2,value3);

                    root.push().setValue(driver);

                    Toast.makeText(getApplicationContext(), "Registered Sucessfully..!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), DriverLogin.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please check your details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLoginText() {

        link = findViewById(R.id.dr_driver_signup_text);

        String loginText = "Already have an account? Login here";

        SpannableString spannableString = new SpannableString(loginText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), DriverLogin.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        link.setText(spannableString);
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
