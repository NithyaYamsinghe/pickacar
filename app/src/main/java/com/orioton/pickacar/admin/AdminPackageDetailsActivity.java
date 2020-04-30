package com.orioton.pickacar.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.orioton.pickacar.R;

public class AdminPackageDetailsActivity extends AppCompatActivity {

    TextView name, price, pricePerKm, Km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_package_details);


        // action bar
        ActionBar actionBar = getSupportActionBar();

        // action bar title
        actionBar.setTitle("Package Details");

        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        name = findViewById(R.id.package_name_text);
        price = findViewById(R.id.package_price_txt);
        pricePerKm = findViewById(R.id.package_price_per_km_text);
        Km = findViewById(R.id.package_km_texts);


        String iName = getIntent().getStringExtra("name");
        String iPrice = getIntent().getStringExtra("price");
        String iPricePerKm = getIntent().getStringExtra("pricePerKm");
        String iKm = getIntent().getStringExtra("km");

        name.setText(iName);
        price.setText(iPrice);
        pricePerKm.setText(iPricePerKm);
        Km.setText(iKm);



    }


    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
