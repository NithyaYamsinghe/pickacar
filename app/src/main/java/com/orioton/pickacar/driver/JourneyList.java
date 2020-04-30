package com.orioton.pickacar.driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orioton.pickacar.R;
import com.orioton.pickacar.driver.model.JourneyModel;

public class JourneyList extends AppCompatActivity {

    RecyclerView recyclerViewJourneyList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference driverRef;
    LinearLayoutManager linearLayoutManager; // for sorting
    SharedPreferences sharedPreferences; // for saving sort settings

    FirebaseRecyclerAdapter<JourneyModel, JourneyLisitViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<JourneyModel> options;

    private Firebase root;

//    FirebaseRecyclerAdapter<JourneyModel, JourneyLisitViewHolder> firebaseRecyclerAdapter;
//    FirebaseRecyclerOptions<JourneyModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_list);

        // action bar
        ActionBar actionBar = getSupportActionBar();

        // set title
        actionBar.setTitle("Journey");

        linearLayoutManager = new LinearLayoutManager(this);

        // recycler view
        recyclerViewJourneyList = findViewById(R.id.recycler_view_journey_list);
        recyclerViewJourneyList.setHasFixedSize(true);


        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        driverRef = firebaseDatabase.getReference("journeys");

        showData();
    }


        // show data
        private void showData() {

            options = new FirebaseRecyclerOptions.Builder<JourneyModel>().setQuery(driverRef, JourneyModel.class).build();
            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<JourneyModel, JourneyLisitViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull JourneyLisitViewHolder journeyLisitViewHolder, int i, @NonNull JourneyModel journeyModel) {
                    journeyLisitViewHolder.setDetails(getApplicationContext(),journeyModel.getUserId(),journeyModel.getUsername(),journeyModel.getPhone(),journeyModel.getDate(),journeyModel.getLocation(),journeyModel.getDestination(),journeyModel.getPassengers(),journeyModel.getStatus(),journeyModel.getPrice());
                }


                @NonNull
                @Override
                public JourneyLisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    // inflating layout car item
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_item, parent, false);
                    JourneyLisitViewHolder journeyListViewHolder = new JourneyLisitViewHolder(itemView);

                    // item click listener
//                    journeyListViewHolder.setOnClickListener(new JourneyLisitViewHolder().ClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//
//                            // get data from firebase at the position clicked
//                            String brandText = getItem(position).getBrand();
//                            String modelText = getItem(position).getModel();
//                            String colorText = getItem(position).getColor();
//                            String releasedYearText = getItem(position).getReleasedYear();
//                            String passengersText = getItem(position).getPassengers();
//                            String descriptionText = getItem(position).getDescription();
//                            String conditionText = getItem(position).getCondition();
//                            String imageText = getItem(position).getImage();
//
//
//                            // pass this data to the new activity
//                            Intent intent = new Intent(view.getContext(), CarDetailsActivity.class);
//                            intent.putExtra("brand", brandText);
//                            intent.putExtra("model", modelText);
//                            intent.putExtra("color", colorText);
//                            intent.putExtra("releasedYear", releasedYearText);
//                            intent.putExtra("passengers", passengersText);
//                            intent.putExtra("description", descriptionText);
//                            intent.putExtra("condition", conditionText);
//                            intent.putExtra("image", imageText);
//                            startActivity(intent);
//
//
//                        }
//
//                        @Override
//                        public void onItemLongClick(View view, int position) {
//                            // get data
//                            final String currentModel = getItem(position).getModel();
//                            final String currentImage = getItem(position).getImage();
//                            final String currentColor = getItem(position).getColor();
//                            final String currentBrand = getItem(position).getBrand();
//                            final String currentReleasedYear = getItem(position).getReleasedYear();
//                            final String currentPassengers = getItem(position).getPassengers();
//                            final String currentCondition = getItem(position).getCondition();
//                            final String currentDescription = getItem(position).getDescription();
//
//                            // show dialog on long click
//                            AlertDialog.Builder builder = new AlertDialog.Builder(CarListActivity.this);
//
//                            // options to display in dialog
//                            String[] options = {"Update", "Delete"};
//
//                            // set to dialog
//                            builder.setItems(options, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    // handle dialog items clicks
//                                    if (which == 0) {
//                                        // update clicked
//
//                                        Intent intent = new Intent(CarListActivity.this, AddNewCarActivity.class);
//                                        intent.putExtra("model", currentModel);
//                                        intent.putExtra("brand", currentBrand);
//                                        intent.putExtra("color", currentColor);
//                                        intent.putExtra("releasedYear", currentReleasedYear);
//                                        intent.putExtra("passengers", currentPassengers);
//                                        intent.putExtra("description", currentDescription);
//                                        intent.putExtra("condition", currentCondition);
//                                        intent.putExtra("image", currentImage);
//                                        startActivity(intent);
//                                    }
//                                    if (which == 1) {
//                                        // delete clicked
//
//
//                                        // method call
//                                        showDeleteDataDialog(currentModel, currentImage);
//                                    }
//                                }
//                            });
//
//                            builder.create().show();
//                        }
//                    });
                    return journeyListViewHolder;
                }
            };


            // set layout as linear layout
            recyclerViewJourneyList.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewJourneyList.setLayoutManager(linearLayoutManager);
            firebaseRecyclerAdapter.startListening();

            // set adapter to firebase recycler view
            recyclerViewJourneyList.setAdapter(firebaseRecyclerAdapter);


        }

}
