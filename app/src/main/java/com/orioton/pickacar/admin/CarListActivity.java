package com.orioton.pickacar.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.orioton.pickacar.R;
import com.orioton.pickacar.model.CarModel;

import java.io.ByteArrayOutputStream;

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
                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getColor(), carModel.getReleasedYear(),

                        carModel.getPassengers(), carModel.getDescription(), carModel.getImage(), carModel.getCondition());

            }

            @Override
            public CarListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CarListViewHolder carListViewHolder = super.onCreateViewHolder(parent, viewType);
                carListViewHolder.setOnClickListener(new CarListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // views
                        TextView brand = view.findViewById(R.id.car_list_brand_txt);
                        TextView model = view.findViewById(R.id.car_list_model_txt);
                        TextView color = view.findViewById(R.id.car_list_color_txt);
                        TextView releasedYear = view.findViewById(R.id.car_list_released_year_txt);
                        TextView passengers = view.findViewById(R.id.car_list_passengers_txt);
                        TextView description = view.findViewById(R.id.car_list_description_txt);
                        TextView condition = view.findViewById(R.id.car_list_condition_txt);
                        ImageView image = view.findViewById(R.id.car_list_image);

                        // get data from views

                        String brandText = brand.getText().toString();
                        String modelText = model.getText().toString();
                        String colorText = color.getText().toString();
                        String releasedYearText = releasedYear.getText().toString();
                        String passengersText = passengers.getText().toString();
                        String descriptionText = description.getText().toString();
                        String conditionText = condition.getText().toString();
                        Drawable drawable = image.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), CarDetailsActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("brand", brandText);
                        intent.putExtra("model", modelText);
                        intent.putExtra("color", colorText);
                        intent.putExtra("releasedYear", releasedYearText);
                        intent.putExtra("passengers", passengersText);
                        intent.putExtra("description", descriptionText);
                        intent.putExtra("condition", conditionText);
                        intent.putExtra("image", bytes);
                        startActivity(intent);



                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return carListViewHolder;
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

                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getColor(),
                        carModel.getReleasedYear(), carModel.getPassengers(), carModel.getDescription(),


                        carModel.getImage(), carModel.getCondition());

            }

            @Override
            public CarListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CarListViewHolder carListViewHolder = super.onCreateViewHolder(parent, viewType);
                carListViewHolder.setOnClickListener(new CarListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // views
                        TextView brand = view.findViewById(R.id.car_list_brand_txt);
                        TextView model = view.findViewById(R.id.car_list_model_txt);
                        TextView color = view.findViewById(R.id.car_list_color_txt);
                        TextView releasedYear = view.findViewById(R.id.car_list_released_year_txt);
                        TextView passengers = view.findViewById(R.id.car_list_passengers_txt);
                        TextView description = view.findViewById(R.id.car_list_description_txt);
                        TextView condition = view.findViewById(R.id.car_list_condition_txt);
                        ImageView image = view.findViewById(R.id.car_list_image);

                        // get data from views

                        String brandText = brand.getText().toString();
                        String modelText = model.getText().toString();
                        String colorText = color.getText().toString();
                        String releasedYearText = releasedYear.getText().toString();
                        String passengersText = passengers.getText().toString();
                        String descriptionText = description.getText().toString();
                        String conditionText = condition.getText().toString();
                        Drawable drawable = image.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), CarDetailsActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("brand", brandText);
                        intent.putExtra("model", modelText);
                        intent.putExtra("color", colorText);
                        intent.putExtra("releasedYear", releasedYearText);
                        intent.putExtra("passengers", passengersText);
                        intent.putExtra("description", descriptionText);
                        intent.putExtra("condition", conditionText);
                        intent.putExtra("image", bytes);
                        startActivity(intent);



                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return carListViewHolder;
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


