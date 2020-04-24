package com.orioton.pickacar.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.orioton.pickacar.R;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {

    // declaring the variables
    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    Button btnSignUp;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initializing the variables
        etUsername = findViewById(R.id.et_user_username);
        etEmail = findViewById(R.id.et_user_email);
        etPassword = findViewById(R.id.et_user_password);
        etConfirmPassword = findViewById(R.id.et_user_ps_confirm);

        btnSignUp = findViewById(R.id.btn_sign_up);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // applying the validations
        awesomeValidation.addValidation(this, R.id.et_user_username,
                RegexTemplate.NOT_EMPTY, R.string.invalid_username);

        awesomeValidation.addValidation(this, R.id.et_user_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.et_user_password,
                ".{8,}", R.string.invalid_password_length);

        awesomeValidation.addValidation(this, R.id.et_user_ps_confirm,
                R.id.et_user_password, R.string.invalid_password_confirm);


        btnSignUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_sign_up):
                if (awesomeValidation.validate()) {
                    // sign the user up if the validation passed

                } else {
                    // validation failed
                    Toast.makeText(getApplicationContext(), "Please check for the errors", Toast.LENGTH_SHORT).show();
                }

        }

    }
}
