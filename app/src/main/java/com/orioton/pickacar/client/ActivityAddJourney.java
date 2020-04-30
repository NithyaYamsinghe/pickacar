package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.orioton.pickacar.client.model.Subscription;
import com.orioton.pickacar.client.model.User;

public class ActivityAddJourney extends AppCompatActivity {

    DatabaseReference databaseReference;

    String databasePath = "journeys";
    String databasePathSub = "subscriptions";

    User currentUser;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    private String currentUserId;
    private String subscriptionKey;

    Subscription subscriptionPlan;

    EditText etLocation;
    EditText etDestination;
    EditText etPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        etLocation = findViewById(R.id.et_location);
        etDestination = findViewById(R.id.et_destination);
        etPassengers = findViewById(R.id.et_passengers);

        progressDialog = new ProgressDialog(ActivityAddJourney.this);

        // showing the progress dialog
        progressDialog.setTitle("Getting the details...");
        progressDialog.show();

        // getting the current userId
        currentUserId = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                    User user = usersSnapshot.getValue(User.class);
                    // getting the current user info from the db snapshot
                    if (user.getUserId().equals(currentUserId)) {

                        // get the current user
                        currentUser = user;
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addJourney(View view) {

        progressDialog.setTitle("Please wait...");
        progressDialog.show();

        // fetching the subscription
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePathSub);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot journeysSnapshot: dataSnapshot.getChildren()) {
                    subscriptionPlan = journeysSnapshot.getValue(Subscription.class);

                    // charge the customer from the subscription
                    charge();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void charge() {
        Integer amount = 100;
        Integer journeyKilometers = 10;

        String packageName = subscriptionPlan.getPackageName();
        Integer kilometers = subscriptionPlan.getKilometers();
        Integer priceKilo = subscriptionPlan.getPricePerKilo();


        Integer currentSubscriptionAmount = subscriptionPlan.getPackagePrice();


        Integer payment = currentSubscriptionAmount - amount;
        Integer leftMiles = kilometers - journeyKilometers;

        Subscription updatedSubscription = new Subscription(currentUserId, packageName, leftMiles, priceKilo, payment);

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePathSub).child(subscriptionKey);
        databaseReference.setValue(updatedSubscription);

        progressDialog.dismiss();

        Toast.makeText(getApplicationContext(), "Journey added!", Toast.LENGTH_SHORT).show();

    }


}
