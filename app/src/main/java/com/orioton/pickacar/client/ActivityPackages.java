package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.orioton.pickacar.client.model.PackageModel;
import com.orioton.pickacar.client.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class ActivityPackages extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceSubs;

    TextView plan1Name;
    TextView plan1Price;
    TextView plan1PricePerKm;
    TextView plan1Kilometers;

    TextView plan2Name;
    TextView plan2Price;
    TextView plan2PricePerKm;
    TextView plan2Kilometers;

    TextView plan3Name;
    TextView plan3Price;
    TextView plan3PricePerKm;
    TextView plan3Kilometers;

    ProgressDialog progressDialog;

    String currentUserId;

    String databasePath = "packages";

    private FirebaseAuth mAuth;


    String databaseSubscriptionPath = "subscriptions";

    List<PackageModel> packages = new ArrayList<PackageModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        mAuth = FirebaseAuth.getInstance();

        // getting the current userId
        currentUserId = mAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(ActivityPackages.this);
        progressDialog.setTitle("Loading packages...");
        progressDialog.show();

        plan1Name = findViewById(R.id.tv_plan1_name);
        plan1Price = findViewById(R.id.tv_plan1_price);
        plan1Kilometers = findViewById(R.id.tv_plan1_kilometers);
        plan1PricePerKm = findViewById(R.id.tv_plan1_price_per_kilo);

        plan2Name = findViewById(R.id.tv_plan2_name);
        plan2Price = findViewById(R.id.tv_plan2_price);
        plan2Kilometers = findViewById(R.id.tv_plan2_kilometers);
        plan2PricePerKm = findViewById(R.id.tv_plan2_price_per_kilo);

        plan3Name = findViewById(R.id.tv_plan3_name);
        plan3Price = findViewById(R.id.tv_plan3_price);
        plan3Kilometers = findViewById(R.id.tv_plan3_kilometers);
        plan3PricePerKm = findViewById(R.id.tv_plan3_price_per_kilo);

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
        databaseReferenceSubs = FirebaseDatabase.getInstance().getReference(databaseSubscriptionPath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot packagesSnapshot: dataSnapshot.getChildren()) {
                    PackageModel myPackage = packagesSnapshot.getValue(PackageModel.class);

                    // adding packages to package list
                    packages.add(myPackage);
                }

                progressDialog.dismiss();
                displayContent();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void displayContent() {
        PackageModel plan1 = packages.get(0);
        PackageModel plan2 = packages.get(1);
        PackageModel plan3 = packages.get(2);


        plan1Name.setText(plan1.getPackageName().toUpperCase() + " PACKAGE");
        plan1Price.setText( Integer.toString(plan1.getPackagePrice().intValue()) + "/-");
        plan1PricePerKm.setText(plan1.getPackagePricePerKm()+ "/1km");
        plan1Kilometers.setText( Integer.toString(plan1.getKilometers().intValue()));

        plan2Name.setText(plan2.getPackageName().toUpperCase() + " PACKAGE");
        plan2Price.setText(Integer.toString(plan2.getPackagePrice().intValue()) + "/-");
        plan2PricePerKm.setText(plan2.getPackagePricePerKm()+ "/1km");
        plan2Kilometers.setText(Integer.toString(plan2.getKilometers().intValue()));

        plan3Name.setText(plan3.getPackageName().toUpperCase() + " PACKAGE");
        plan3Price.setText(Integer.toString(plan3.getPackagePrice().intValue()) + "/-");
        plan3PricePerKm.setText(plan3.getPackagePricePerKm()+ "/1km");
        plan3Kilometers.setText(Integer.toString(plan3.getKilometers().intValue()));


    }

    public void subscribePlan1(View view) {
        PackageModel plan1 = packages.get(0);
        String packageName = plan1.getPackageName();
        Integer kilometers = plan1.getKilometers().intValue();
        Integer pricePerKilo = plan1.getPackagePricePerKm().intValue();
        Integer packagePrice = plan1.getPackagePrice().intValue();

        Subscription subscription = new Subscription(currentUserId, packageName, kilometers, pricePerKilo, packagePrice);
        String id = databaseReferenceSubs.push().getKey();
        databaseReferenceSubs.child(id).setValue(subscription);

        Toast.makeText(getApplicationContext(), "Subscription Added", Toast.LENGTH_SHORT).show();

    }

    public void subscribePlan2(View view) {
        PackageModel plan2 = packages.get(1);
        String packageName = plan2.getPackageName();
        Integer kilometers = plan2.getKilometers().intValue();
        Integer pricePerKilo = plan2.getPackagePricePerKm().intValue();
        Integer packagePrice = plan2.getPackagePrice().intValue();

        Subscription subscription = new Subscription(currentUserId, packageName, kilometers, pricePerKilo, packagePrice);
        String id = databaseReferenceSubs.push().getKey();
        databaseReferenceSubs.child(id).setValue(subscription);

        Toast.makeText(getApplicationContext(), "Subscription Added", Toast.LENGTH_SHORT).show();

    }

    public void subscribePlan3(View view) {
        PackageModel plan3 = packages.get(2);
        String packageName = plan3.getPackageName();
        Integer kilometers = plan3.getKilometers().intValue();
        Integer pricePerKilo = plan3.getPackagePricePerKm().intValue();
        Integer packagePrice = plan3.getPackagePrice().intValue();

        Subscription subscription = new Subscription(currentUserId, packageName, kilometers, pricePerKilo, packagePrice);
        String id = databaseReferenceSubs.push().getKey();
        databaseReferenceSubs.child(id).setValue(subscription);

        Toast.makeText(getApplicationContext(), "Subscription Added", Toast.LENGTH_SHORT).show();

    }


}
