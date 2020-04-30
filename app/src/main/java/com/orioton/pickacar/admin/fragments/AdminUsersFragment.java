package com.orioton.pickacar.admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.AdminUserModel;
import com.orioton.pickacar.admin.views.AdminUsersViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdminUsersViewHolder adminUsersViewHolder;
    List<AdminUserModel> userList;

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

        // init user list



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
}
