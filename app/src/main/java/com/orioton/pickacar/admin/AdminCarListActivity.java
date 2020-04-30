package com.orioton.pickacar.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.CarModel;
import com.orioton.pickacar.admin.adapters.AdminCarListViewHolder;


public class AdminCarListActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager; // for sorting
    SharedPreferences sharedPreferences; // for saving sort settings
    RecyclerView recyclerViewCarList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference adminRef;
    FirebaseRecyclerAdapter<CarModel, AdminCarListViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<CarModel> options;

    // firebase auth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_car_list);



        // init
        firebaseAuth = FirebaseAuth.getInstance();

        // action bar
        ActionBar actionBar = getSupportActionBar();

        // set title
        actionBar.setTitle("Cars");

        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        sharedPreferences = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String sorting = sharedPreferences.getString("Sort", "newest"); // default will be newest

        if (sorting.equals("newest")) {
            linearLayoutManager = new LinearLayoutManager(this);
            // bring items from bottom means newest
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        } else if (sorting.equals("oldest")) {
            linearLayoutManager = new LinearLayoutManager(this);
            // bring items from bottom means oldest
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }


        // recycler view
        recyclerViewCarList = findViewById(R.id.recycler_view_car_list);
        recyclerViewCarList.setHasFixedSize(true);


        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        adminRef = firebaseDatabase.getReference("cars");

        showData();

    }

    // delete data
    private void showDeleteDataDialog(final String currentModel, final String currentImage) {

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCarListActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure ?");

        // set positive button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = adminRef.orderByChild("image").equalTo(currentImage);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue(); // delete details from firebase
                        }

                        // confirm deletion
                        Toast.makeText(AdminCarListActivity.this, "car deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        // if anything went wrong
                        Toast.makeText(AdminCarListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


                // delete image from firebase storage using reference url
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentImage);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // delete successful
                        Toast.makeText(AdminCarListActivity.this, "image deleted successfully", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // something went wrong
                        Toast.makeText(AdminCarListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        // negative button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        // show dialog
        builder.create().show();


    }

    // show data
    private void showData() {

        options = new FirebaseRecyclerOptions.Builder<CarModel>().setQuery(adminRef, CarModel.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CarModel, AdminCarListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCarListViewHolder carListViewHolder, int i, @NonNull CarModel carModel) {

                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getColor(), carModel.getReleasedYear(),
                        carModel.getPassengers(), carModel.getDescription(), carModel.getImage(), carModel.getCondition());

            }

            @NonNull
            @Override
            public AdminCarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // inflating layout car item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_car_item, parent, false);
                AdminCarListViewHolder carListViewHolder = new AdminCarListViewHolder(itemView);

                // item click listener
                carListViewHolder.setOnClickListener(new AdminCarListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        // get data from firebase at the position clicked
                        String brandText = getItem(position).getBrand();
                        String modelText = getItem(position).getModel();
                        String colorText = getItem(position).getColor();
                        String releasedYearText = getItem(position).getReleasedYear();
                        String passengersText = getItem(position).getPassengers();
                        String descriptionText = getItem(position).getDescription();
                        String conditionText = getItem(position).getCondition();
                        String imageText = getItem(position).getImage();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), AdminCarDetailsActivity.class);
                        intent.putExtra("brand", brandText);
                        intent.putExtra("model", modelText);
                        intent.putExtra("color", colorText);
                        intent.putExtra("releasedYear", releasedYearText);
                        intent.putExtra("passengers", passengersText);
                        intent.putExtra("description", descriptionText);
                        intent.putExtra("condition", conditionText);
                        intent.putExtra("image", imageText);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // get data
                        final String currentModel = getItem(position).getModel();
                        final String currentImage = getItem(position).getImage();
                        final String currentColor = getItem(position).getColor();
                        final String currentBrand = getItem(position).getBrand();
                        final String currentReleasedYear = getItem(position).getReleasedYear();
                        final String currentPassengers = getItem(position).getPassengers();
                        final String currentCondition = getItem(position).getCondition();
                        final String currentDescription = getItem(position).getDescription();

                        // show dialog on long click
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCarListActivity.this);

                        // options to display in dialog
                        String[] options = {"Update", "Delete"};

                        // set to dialog
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // handle dialog items clicks
                                if (which == 0) {
                                    // update clicked

                                    Intent intent = new Intent(AdminCarListActivity.this, AdminAddNewCarActivity.class);
                                    intent.putExtra("model", currentModel);
                                    intent.putExtra("brand", currentBrand);
                                    intent.putExtra("color", currentColor);
                                    intent.putExtra("releasedYear", currentReleasedYear);
                                    intent.putExtra("passengers", currentPassengers);
                                    intent.putExtra("description", currentDescription);
                                    intent.putExtra("condition", currentCondition);
                                    intent.putExtra("image", currentImage);
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    // delete clicked


                                    // method call
                                    showDeleteDataDialog(currentModel, currentImage);
                                }
                            }
                        });

                        builder.create().show();
                    }
                });
                return carListViewHolder;
            }
        };


        // set layout as linear layout
        //recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarList.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();

        // set adapter to firebase recycler view
        recyclerViewCarList.setAdapter(firebaseRecyclerAdapter);


    }

    // search data
    private void firebaseSearch(String searchText) {

        // convert string entered to search view in to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = adminRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<CarModel>().setQuery(firebaseSearchQuery, CarModel.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CarModel, AdminCarListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCarListViewHolder carListViewHolder, int i, @NonNull CarModel carModel) {

                carListViewHolder.setDetails(getApplicationContext(), carModel.getModel(), carModel.getBrand(), carModel.getColor(), carModel.getReleasedYear(),
                        carModel.getPassengers(), carModel.getDescription(), carModel.getImage(), carModel.getCondition());

            }

            @NonNull
            @Override
            public AdminCarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // inflating layout car item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_car_item, parent, false);
                AdminCarListViewHolder carListViewHolder = new AdminCarListViewHolder(itemView);

                // item click listener
                carListViewHolder.setOnClickListener(new AdminCarListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        // get data from firebase at the position clicked
                        String brandText = getItem(position).getBrand();
                        String modelText = getItem(position).getModel();
                        String colorText = getItem(position).getColor();
                        String releasedYearText = getItem(position).getReleasedYear();
                        String passengersText = getItem(position).getPassengers();
                        String descriptionText = getItem(position).getDescription();
                        String conditionText = getItem(position).getCondition();
                        String imageText = getItem(position).getImage();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), AdminCarDetailsActivity.class);
                        intent.putExtra("brand", brandText);
                        intent.putExtra("model", modelText);
                        intent.putExtra("color", colorText);
                        intent.putExtra("releasedYear", releasedYearText);
                        intent.putExtra("passengers", passengersText);
                        intent.putExtra("description", descriptionText);
                        intent.putExtra("condition", conditionText);
                        intent.putExtra("image", imageText);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        // get data
                        final String currentModel = getItem(position).getModel();
                        final String currentImage = getItem(position).getImage();
                        final String currentColor = getItem(position).getColor();
                        final String currentBrand = getItem(position).getBrand();
                        final String currentReleasedYear = getItem(position).getReleasedYear();
                        final String currentPassengers = getItem(position).getPassengers();
                        final String currentCondition = getItem(position).getCondition();
                        final String currentDescription = getItem(position).getDescription();

                        // show dialog on long click
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCarListActivity.this);

                        // options to display in dialog
                        String[] options = {"Update", "Delete"};

                        // set to dialog
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // handle dialog items clicks
                                if (which == 0) {
                                    // update clicked

                                    Intent intent = new Intent(AdminCarListActivity.this, AdminAddNewCarActivity.class);
                                    intent.putExtra("model", currentModel);
                                    intent.putExtra("brand", currentBrand);
                                    intent.putExtra("color", currentColor);
                                    intent.putExtra("releasedYear", currentReleasedYear);
                                    intent.putExtra("passengers", currentPassengers);
                                    intent.putExtra("description", currentDescription);
                                    intent.putExtra("condition", currentCondition);
                                    intent.putExtra("image", currentImage);
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    // delete clicked


                                    // method call
                                    showDeleteDataDialog(currentModel, currentImage);
                                }
                            }
                        });

                        builder.create().show();

                    }
                });

                return carListViewHolder;
            }
        };


        // set layout as linear layout
        //recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarList.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();

        // set adapter to firebase recycler view
        recyclerViewCarList.setAdapter(firebaseRecyclerAdapter);

    }

    // load data in to recycler view
    @Override
    protected void onStart() {

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();

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
        if (id == R.id.action_sort) {

            // alert dialog
            showSortDialog();
            return true;
        }

        if (id == R.id.action_add) {

            // start activity
            startActivity(new Intent(AdminCarListActivity.this, AdminAddNewCarActivity.class));
            return true;
        }
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(AdminCarListActivity.this, MainActivity.class));
        }


    }
    private void showSortDialog() {

        // options to display in dialog
        String[] sortOptions = {"Newest", "Oldest"};

        // create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by").setIcon(R.drawable.ic_action_sort) // set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // sort by newest
                            // edit shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "newest");
                            editor.apply(); // apply save the value
                            recreate(); // restart activity to take effect


                        } else if (which == 1) {
                            // sort by oldest
                            // edit shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "oldest");
                            editor.apply(); // apply save the value
                            recreate(); // restart activity to take effect

                        }

                    }
                });

        builder.show();
    }

    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


