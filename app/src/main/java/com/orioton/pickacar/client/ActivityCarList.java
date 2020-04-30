package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.CarModel;
import com.squareup.picasso.Picasso;

public class ActivityCarList extends AppCompatActivity {

    FirebaseRecyclerOptions<CarModel> options;
    FirebaseRecyclerAdapter<CarModel, CarViewHolder> adapter;

    RecyclerView recyclerViewCarList;

    DatabaseReference databaseReference;
    String databasePath = "cars";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        setTitle("Select A Car");

        recyclerViewCarList = findViewById(R.id.rv_car_list);
        recyclerViewCarList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewCarList.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(databasePath);

        loadCars();

    }

    private void loadCars() {
        options = new FirebaseRecyclerOptions.Builder<CarModel>().setQuery(databaseReference, CarModel.class).build();

        // initializing the adapter
        adapter = new FirebaseRecyclerAdapter<CarModel, CarViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int i, @NonNull CarModel carModel) {
                carViewHolder.carName.setText(carModel.getModel());
                Picasso.get().load(carModel.getImage()).into(carViewHolder.carImage);
            }

            @NonNull
            @Override
            public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_car_single, parent, false);

                return new CarViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerViewCarList.setAdapter(adapter);

    }
}
