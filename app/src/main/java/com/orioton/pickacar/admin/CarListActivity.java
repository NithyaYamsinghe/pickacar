package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orioton.pickacar.R;
import com.orioton.pickacar.model.CarModel;

public class CarListActivity extends AppCompatActivity {

    RecyclerView recyclerViewCarList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);


        // action bar
        ActionBar actionBar = getSupportActionBar();

        // set title
        actionBar.setTitle("cars");

        // recycler view
        recyclerViewCarList = findViewById(R.id.recycler_view_car_list);
        recyclerViewCarList.setHasFixedSize(true);

        // set layout as linear layout
        recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        adminRef = firebaseDatabase.getReference("cars");

    }


    // load data in to recycler view

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CarModel, CarListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CarModel, CarListViewHolder>(
                CarModel.class,
                R.layout.layout_car_item,
                CarListViewHolder.class,
                adminRef
        ) {
            @Override
            protected void populateViewHolder(CarListViewHolder carListViewHolder, CarModel carModel, int i) {

                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getImage());

            }
        };

        // set adapter to recycler view
        recyclerViewCarList.setAdapter(firebaseRecyclerAdapter);

    }


}


