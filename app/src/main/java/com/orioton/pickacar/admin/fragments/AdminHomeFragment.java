package com.orioton.pickacar.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.AddNewCarActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {
    // firebase auth
    FirebaseAuth firebaseAuth;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // init
        firebaseAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // show menu

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_car_list_menu, menu);
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
