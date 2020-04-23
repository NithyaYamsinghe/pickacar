package com.orioton.pickacar.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.orioton.pickacar.R;

public class ActivityAddCar extends AppCompatActivity {

    // initializing variables
    EditText carBrand, carModel, carColor, carReleasedYear, carPassangers, carDescription;
    Button buttonAddCar;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // assigning the variables
        carBrand = findViewById(R.id.et_car_brand);
        carModel = findViewById(R.id.et_car_model);
        carColor = findViewById(R.id.et_car_color);
        carPassangers = findViewById(R.id.et_car_passengers);
        carReleasedYear = findViewById(R.id.et_car_released_year);
        carDescription = findViewById(R.id.et_car_description);

        buttonAddCar = findViewById(R.id.btn_add_car);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // applying the validations
        awesomeValidation.addValidation(this, R.id.et_car_brand,
                RegexTemplate.NOT_EMPTY, R.string.invalid_brand_name);

        awesomeValidation.addValidation(this, R.id.et_car_model,
                RegexTemplate.NOT_EMPTY, R.string.invalid_model_name);

        awesomeValidation.addValidation(this, R.id.et_car_color,
                RegexTemplate.NOT_EMPTY, R.string.invalid_color_name);

        awesomeValidation.addValidation(this, R.id.et_car_passengers,
                RegexTemplate.NOT_EMPTY, R.string.invalid_passangers);

        awesomeValidation.addValidation(this, R.id.et_car_released_year,
                RegexTemplate.NOT_EMPTY, R.string.invalid_released_year);

        awesomeValidation.addValidation(this, R.id.et_car_description,
                RegexTemplate.NOT_EMPTY, R.string.invalid_description);


        buttonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for the validations
                if (awesomeValidation.validate()) {
                    // validation passed
                    Toast.makeText(getApplicationContext(), "Validation passed!", Toast.LENGTH_SHORT).show();
                } else {
                    // validation failed
                    Toast.makeText(getApplicationContext(), "Validation failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
