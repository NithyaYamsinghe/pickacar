package com.orioton.pickacar.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.AddNewCarActivity;
import com.orioton.pickacar.admin.model.AdminUserModel;
import com.orioton.pickacar.admin.adapters.AdminUsersViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdminUsersViewHolder adminUsersViewHolder;
    List<AdminUserModel> userList;

    // firebase auth
    FirebaseAuth firebaseAuth;

    public AdminUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);


        // init recycler view
        recyclerView = view.findViewById(R.id.users_recycler_view);

        // init
        firebaseAuth = FirebaseAuth.getInstance();



        // set properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // init user list
        userList = new ArrayList<>();

        // get all users
        getAllUsers();


        return  view;
    }

    private void getAllUsers() {

        // get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adminUsers");

        // get all data from path

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    AdminUserModel adminUserModel = ds.getValue(AdminUserModel.class);

                    // get all users except signed in user
                    if (!adminUserModel.getUid().equals(firebaseUser.getUid())){

                        userList.add(adminUserModel);
                    }
                    adminUsersViewHolder = new AdminUsersViewHolder(getActivity(), userList);
                    recyclerView.setAdapter(adminUsersViewHolder);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













    }

    private void searchUsers(final String query) {
        // get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("adminUsers");

        // get all data from path

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    AdminUserModel adminUserModel = ds.getValue(AdminUserModel.class);

                    // get all users except signed in user
                    if (!adminUserModel.getUid().equals(firebaseUser.getUid())){

                        if (adminUserModel.getName().toLowerCase().contains(query.toLowerCase())
                                || adminUserModel.getEmail().toLowerCase().contains(query.toLowerCase())){

                            userList.add(adminUserModel);
                        }


                    }
                    adminUsersViewHolder = new AdminUsersViewHolder(getActivity(), userList);

                    // refresh
                    adminUsersViewHolder.notifyDataSetChanged();
                    recyclerView.setAdapter(adminUsersViewHolder);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // show menu

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_car_list_menu, menu);

        // search

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);


        // search listener


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (! TextUtils.isEmpty(query.trim())){

                    // search
                    searchUsers(query);

                }
                else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (! TextUtils.isEmpty(newText.trim())){

                    // search
                    searchUsers(newText);

                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();

        } else if (id == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddNewCarActivity.class));


        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }


    }
}
