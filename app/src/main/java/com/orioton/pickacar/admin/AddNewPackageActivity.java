package com.orioton.pickacar.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.PackageModel;

public class AddNewPackageActivity extends AppCompatActivity {

    TextInputEditText admin_package_name, admin_package_price, admin_package_price_per_km;
    Button add_package;

    // root database name for firebase database
    String databasePath = "packages";

    // creating storage reference and database reference

    DatabaseReference databaseReference;

    // declare an instance of firebaseAuth
    private FirebaseAuth firebaseAuth;

    // progress dialog
    ProgressDialog progressDialog;

    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_package);


        // action bar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Package");

        // initializing
        admin_package_name = findViewById(R.id.admin_add_package_name);
        admin_package_price = findViewById(R.id.admin_add_package_price);
        admin_package_price_per_km = findViewById(R.id.admin_add_price_kilo);
        add_package = findViewById(R.id.admin_add_package_button);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);




        // applying the validations
        awesomeValidation.addValidation(this, R.id.admin_add_package_name, RegexTemplate.NOT_EMPTY, R.string.invalid_package_name);
        awesomeValidation.addValidation(this, R.id.admin_add_package_price, RegexTemplate.NOT_EMPTY, R.string.invalid_package_price);
        awesomeValidation.addValidation(this, R.id.admin_add_price_kilo, RegexTemplate.NOT_EMPTY, R.string.invalid_package_price_per_km);

        // assign firebase database instance to firebase storage database object
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        // progress dialog
        progressDialog = new ProgressDialog(AddNewPackageActivity.this);
        // In the onCreate() method, initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();



        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call method to upload data to firebase
                uploadDataToFirebase();

            }
        });


    }

    private void uploadDataToFirebase() {


        if (awesomeValidation.validate()) {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            String uid = user.getUid();


            String package_name = admin_package_name.getText().toString().trim();
            String package_price = admin_package_price.getText().toString().trim();
            String package_price_per_km = admin_package_price_per_km.getText().toString().trim();

            PackageModel packageModel = new PackageModel(package_name, Double.parseDouble(package_price), Double.parseDouble(package_price_per_km), uid);
            String uploadId = databaseReference.push().getKey();
            databaseReference.child(uploadId).setValue(packageModel);


        }


    }
}
