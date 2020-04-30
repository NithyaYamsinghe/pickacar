package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.PackageModel;

public class AdminAddNewPackageActivity extends AppCompatActivity {

    TextInputEditText admin_package_name, admin_package_price, admin_package_price_per_km, admin_package_kilometers;
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

    // intent data will be stored here
    String edit_admin_package_name;
    Double edit_admin_package_price, edit_admin_package_price_per_km, edit_admin_package_kilometers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_package);


        // action bar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Package");

        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // initializing
        admin_package_name = findViewById(R.id.admin_add_package_name);
        admin_package_price = findViewById(R.id.admin_add_package_price);
        admin_package_price_per_km = findViewById(R.id.admin_add_price_kilo);
        admin_package_kilometers = findViewById(R.id.admin_add_package_km);
        add_package = findViewById(R.id.admin_add_package_button);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        // applying the validations
        awesomeValidation.addValidation(this, R.id.admin_add_package_name, RegexTemplate.NOT_EMPTY, R.string.invalid_package_name);
        awesomeValidation.addValidation(this, R.id.admin_add_package_price, RegexTemplate.NOT_EMPTY, R.string.invalid_package_price);
        awesomeValidation.addValidation(this, R.id.admin_add_price_kilo, RegexTemplate.NOT_EMPTY, R.string.invalid_package_price_per_km);
        awesomeValidation.addValidation(this, R.id.admin_add_package_km, RegexTemplate.NOT_EMPTY, R.string.invalid_package_km);

        // assign firebase database instance to firebase storage database object
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        // progress dialog
        progressDialog = new ProgressDialog(AdminAddNewPackageActivity.this);
        // In the onCreate() method, initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();


        // get data from intent if not null
        Bundle intent = getIntent().getExtras();
        if (intent != null) {

            edit_admin_package_name = intent.getString("name");
            edit_admin_package_price = intent.getDouble("price");
            edit_admin_package_price_per_km = intent.getDouble("pricePerKm");
            edit_admin_package_kilometers = intent.getDouble("kilometers");


            // set these data to views
            admin_package_name.setText(edit_admin_package_name);
            admin_package_price.setText(String.valueOf(edit_admin_package_price));
            admin_package_price_per_km.setText(String.valueOf(edit_admin_package_price_per_km));
            admin_package_kilometers.setText(String.valueOf(edit_admin_package_kilometers));


            // change action bar and the button
            actionBar.setTitle("Update package");
            add_package.setText("Update");


        }


        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_package.getText().equals("Add package")) {


                    // call method to upload data to firebase
                    uploadDataToFirebase();

                } else {

                    updateData();

                }


            }
        });


    }

    private void updateData() {

        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        updateDatabase();


    }

    private void updateDatabase() {


        // new values to update

        final String addName = admin_package_name.getText().toString().trim();
        final String addPrice = admin_package_price.getText().toString().trim();
        final String addPricePerKm = admin_package_price_per_km.getText().toString().trim();
        final String addKm = admin_package_kilometers.getText().toString().trim();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceNew = firebaseDatabase.getReference("packages");

        Query query = databaseReferenceNew.orderByChild("packageName").equalTo(edit_admin_package_name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ds.getRef().child("packageName").setValue(addName);
                    ds.getRef().child("kilometers").setValue(Double.parseDouble(addKm));
                    ds.getRef().child("packagePrice").setValue(Double.parseDouble(addPrice));
                    ds.getRef().child("packagePricePerKm").setValue(Double.parseDouble(addPricePerKm));

                }
                progressDialog.dismiss();

                // start car list after update
                Toast.makeText(AdminAddNewPackageActivity.this, "database updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminAddNewPackageActivity.this, AdminPackageListActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            String package_km = admin_package_kilometers.getText().toString().trim();

            progressDialog.dismiss();

            PackageModel packageModel = new PackageModel(package_name, Double.parseDouble(package_price), Double.parseDouble(package_price_per_km), Double.parseDouble(package_km), uid);
            String uploadId = databaseReference.push().getKey();
            databaseReference.child(uploadId).setValue(packageModel);

            startActivity(new Intent(AdminAddNewPackageActivity.this, AdminPackageListActivity.class));


        }


    }


    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
