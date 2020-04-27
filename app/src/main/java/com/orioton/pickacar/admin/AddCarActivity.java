package com.orioton.pickacar.admin;

import androidx.appcompat.app.AppCompatActivity;

public class AddCarActivity extends AppCompatActivity {

//    // initializing variables
//    EditText carBrand, carModel, carColor, carReleasedYear, carPassangers, carDescription;
//    Button buttonAddCar;
//
//    AwesomeValidation awesomeValidation;
//
//    DatabaseReference databasePickACar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_car);
//
//        // assigning the variables
//        carBrand = findViewById(R.id.et_car_brand);
//        carModel = findViewById(R.id.et_car_model);
//        carColor = findViewById(R.id.et_car_color);
//        carPassangers = findViewById(R.id.et_car_passengers);
//        carReleasedYear = findViewById(R.id.et_car_released_year);
//        carDescription = findViewById(R.id.et_car_description);
//
//        buttonAddCar = findViewById(R.id.btn_add_car);
//
//        // initializing validation style
//        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
//
//        // initializing database reference
//        databasePickACar = FirebaseDatabase.getInstance().getReference("cars");
//
//        // applying the validations
//        awesomeValidation.addValidation(this, R.id.et_car_brand,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_brand_name);
//
//        awesomeValidation.addValidation(this, R.id.et_car_model,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_model_name);
//
//        awesomeValidation.addValidation(this, R.id.et_car_color,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_color_name);
//
//        awesomeValidation.addValidation(this, R.id.et_car_passengers,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_passangers);
//
//        awesomeValidation.addValidation(this, R.id.et_car_released_year,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_released_year);
//
//        awesomeValidation.addValidation(this, R.id.et_car_description,
//                RegexTemplate.NOT_EMPTY, R.string.invalid_description);
//
//
//
//
//
//        buttonAddCar.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // checking for the validations
//                if (awesomeValidation.validate()) {
//
//                    // validation passed
//                    String carBrandVal = carBrand.getText().toString().trim();
//                    String carModelVal = carModel.getText().toString().trim();
//                    String carColorVal = carColor.getText().toString().trim();
//                    String carDescriptionVal = carDescription.getText().toString().trim();
//                    String carReleasedYearVal = carReleasedYear.getText().toString().trim();
//                    String carPassengersVal = carPassangers.getText().toString().trim();
//
//                    String id = databasePickACar.push().getKey();
//                    CarModel car = new CarModel(carBrandVal, carModelVal, carColorVal, Integer.parseInt(carReleasedYearVal), Integer.parseInt(carPassengersVal), carDescriptionVal, c);
//                    databasePickACar.child(id).setValue(car);
//                    Toast.makeText(getApplicationContext(), "Car added!", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    // validation failed
//                    Toast.makeText(getApplicationContext(), "Validation failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }


}
