package com.orioton.pickacar.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
        actionBar.setTitle("Cars");

        // recycler view
        recyclerViewCarList = findViewById(R.id.recycler_view_car_list);
        recyclerViewCarList.setHasFixedSize(true);

        // set layout as linear layout
        recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        adminRef = firebaseDatabase.getReference("cars");

    }


    // search data
    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = adminRef.orderByChild("model").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<CarModel, CarListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CarModel, CarListViewHolder>(

                CarModel.class,
                R.layout.layout_car_item,
                CarListViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(CarListViewHolder carListViewHolder, CarModel carModel, int i) {
                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getImage());

            }


        };
        // set adapter to recycler view
        recyclerViewCarList.setAdapter(firebaseRecyclerAdapter);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate menu

        getMenuInflater().inflate(R.menu.admin_car_list_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter as type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // handle other action bar items
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


