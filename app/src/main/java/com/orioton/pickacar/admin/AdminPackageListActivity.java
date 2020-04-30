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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.PackageModel;
import com.orioton.pickacar.admin.adapters.AdminPackageListViewHolder;

public class AdminPackageListActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager; // for sorting
    SharedPreferences sharedPreferences; // for saving sort settings
    RecyclerView recyclerViewPackageList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference adminRef;
    FirebaseRecyclerAdapter<PackageModel, AdminPackageListViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<PackageModel> options;

    // firebase auth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_package_list);


        // init
        firebaseAuth = FirebaseAuth.getInstance();

        // action bar
        ActionBar actionBar = getSupportActionBar();

        // set title
        actionBar.setTitle("Packages");
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
        recyclerViewPackageList = findViewById(R.id.recycler_view_package_list);
        recyclerViewPackageList.setHasFixedSize(true);


        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        adminRef = firebaseDatabase.getReference("packages");

        showData();

    }

    // show data
    private void showData() {


        options = new FirebaseRecyclerOptions.Builder<PackageModel>().setQuery(adminRef, PackageModel.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PackageModel, AdminPackageListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminPackageListViewHolder adminPackageListViewHolder, int i, @NonNull PackageModel packageModel) {

                adminPackageListViewHolder.setDetails(getApplicationContext(), packageModel.getPackageName(), packageModel.getPackagePrice(), packageModel.getPackagePricePerKm(),
                        packageModel.getKilometers());

            }

            @NonNull
            @Override
            public AdminPackageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // inflating layout car item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_package_item, parent, false);
                AdminPackageListViewHolder packageListViewHolder = new AdminPackageListViewHolder(itemView);

                // item click listener
                packageListViewHolder.setOnClickListener(new AdminPackageListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        // get data from firebase at the position clicked
                        String nameText = getItem(position).getPackageName();
                        Double priceText = getItem(position).getPackagePrice();
                        Double pricePerKmText = getItem(position).getPackagePricePerKm();
                        Double kmText = getItem(position).getKilometers();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), AdminPackageDetailsActivity.class);
                        intent.putExtra("name", nameText);
                        intent.putExtra("price", priceText);
                        intent.putExtra("pricePerKm", pricePerKmText);
                        intent.putExtra("km", kmText);

                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // get data
                        final String currentName = getItem(position).getPackageName();
                        final Double currentPrice = getItem(position).getPackagePrice();
                        final Double currentPricePerKm = getItem(position).getPackagePricePerKm();
                        final Double currentKilometers = getItem(position).getKilometers();


                        // show dialog on long click
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPackageListActivity.this);

                        // options to display in dialog
                        String[] options = {"Update", "Delete"};

                        // set to dialog
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // handle dialog items clicks
                                if (which == 0) {
                                    // update clicked

                                    Intent intent = new Intent(AdminPackageListActivity.this, AddNewPackageActivity.class);
                                    intent.putExtra("name", currentName);
                                    intent.putExtra("price", currentPrice);
                                    intent.putExtra("pricePerKm", currentPricePerKm);
                                    intent.putExtra("kilometers", currentKilometers);

                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    // delete clicked


                                    // method call
                                    showDeleteDataDialog(currentName);
                                }
                            }
                        });

                        builder.create().show();
                    }
                });
                return packageListViewHolder;
            }
        };


        // set layout as linear layout
        //recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPackageList.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();

        // set adapter to firebase recycler view
        recyclerViewPackageList.setAdapter(firebaseRecyclerAdapter);

    }


    // delete data
    private void showDeleteDataDialog(final String currentName) {


        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPackageListActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure ?");

        // set positive button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = adminRef.orderByChild("packageName").equalTo(currentName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue(); // delete details from firebase
                        }

                        // confirm deletion
                        Toast.makeText(AdminPackageListActivity.this, "package deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        // if anything went wrong
                        Toast.makeText(AdminPackageListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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


    // search data
    private void firebaseSearch(String searchText) {

        // convert string entered to search view in to lowercase
        //String query = searchText.toLowerCase();
        String query = searchText;

        Query firebaseSearchQuery = adminRef.orderByChild("packageName").startAt(query).endAt(query + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<PackageModel>().setQuery(firebaseSearchQuery, PackageModel.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PackageModel, AdminPackageListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminPackageListViewHolder packageListViewHolder, int i, @NonNull PackageModel packageModel) {


                packageListViewHolder.setDetails(getApplicationContext(), packageModel.getPackageName(), packageModel.getPackagePrice(), packageModel.getPackagePricePerKm(),
                        packageModel.getKilometers());

            }

            @NonNull
            @Override
            public AdminPackageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // inflating layout car item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_package_item, parent, false);
                AdminPackageListViewHolder packageListViewHolder = new AdminPackageListViewHolder(itemView);

                // item click listener
                packageListViewHolder.setOnClickListener(new AdminPackageListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        // get data from firebase at the position clicked
                        String nameText = getItem(position).getPackageName();
                        Double priceText = getItem(position).getPackagePrice();
                        Double pricePerKmText = getItem(position).getPackagePricePerKm();
                        Double kmText = getItem(position).getKilometers();


                        // pass this data to the new activity
                        Intent intent = new Intent(view.getContext(), AdminPackageDetailsActivity.class);
                        intent.putExtra("name", nameText);
                        intent.putExtra("price", priceText);
                        intent.putExtra("pricePerKm", pricePerKmText);
                        intent.putExtra("km", kmText);

                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        // get data
                        final String currentName = getItem(position).getPackageName();
                        final Double currentPrice = getItem(position).getPackagePrice();
                        final Double currentPricePerKm = getItem(position).getPackagePricePerKm();
                        final Double currentKilometers = getItem(position).getKilometers();

                        // show dialog on long click
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPackageListActivity.this);

                        // options to display in dialog
                        String[] options = {"Update", "Delete"};

                        // set to dialog
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // handle dialog items clicks
                                if (which == 0) {
                                    // update clicked

                                    Intent intent = new Intent(AdminPackageListActivity.this, AddNewPackageActivity.class);
                                    intent.putExtra("name", currentName);
                                    intent.putExtra("price", currentPrice);
                                    intent.putExtra("pricePerKm", currentPricePerKm);
                                    intent.putExtra("kilometers", currentKilometers);
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    // delete clicked


                                    // method call
                                    showDeleteDataDialog(currentName);
                                }
                            }
                        });

                        builder.create().show();

                    }
                });

                return packageListViewHolder;
            }
        };


        // set layout as linear layout
        //recyclerViewCarList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPackageList.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();

        // set adapter to firebase recycler view
        recyclerViewPackageList.setAdapter(firebaseRecyclerAdapter);

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
            startActivity(new Intent(AdminPackageListActivity.this, AddNewPackageActivity.class));
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
            startActivity(new Intent(AdminPackageListActivity.this, MainActivity.class));
        }


    }

    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
