package com.orioton.pickacar.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.orioton.pickacar.R;

public class CarDetailsActivity extends AppCompatActivity {

    TextView brand, model, color, releasedYear, passenger, description, condition;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        // action bar
        ActionBar actionBar = getSupportActionBar();

        // action bar title
        actionBar.setTitle("Car Details");

        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // initialize views
        brand = findViewById(R.id.car_detail_brand_txt);
        model = findViewById(R.id.car_detail_model_txt);
        color = findViewById(R.id.car_detail_color_txt);
        releasedYear = findViewById(R.id.car_detail_year_txt);
        passenger = findViewById(R.id.car_detail_passengers_txt);
        description = findViewById(R.id.car_detail_description_txt);
        condition = findViewById(R.id.car_detail_condition_txt);
        image = findViewById(R.id.car_detail_image);


        // get data from intent

        byte[] bytes = getIntent().getByteArrayExtra("image");
        String models = getIntent().getStringExtra("model");
        String brands = getIntent().getStringExtra("brand");
        String colors = getIntent().getStringExtra("color");
        String releasedYears = getIntent().getStringExtra("releasedYear");
        String passengers = getIntent().getStringExtra("passengers");
        String descriptions = getIntent().getStringExtra("description");
        String conditions = getIntent().getStringExtra("condition");
        Bitmap bitmaps = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        // set data to views
        brand.setText(brands);
        model.setText(models);
        color.setText(colors);
        releasedYear.setText(releasedYears);
        passenger.setText(passengers);
        description.setText(descriptions);
        condition.setText(conditions);
        image.setImageBitmap(bitmaps);


    }


    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
