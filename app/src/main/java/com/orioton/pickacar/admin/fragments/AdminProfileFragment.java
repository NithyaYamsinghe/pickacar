package com.orioton.pickacar.admin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminProfileFragment extends Fragment {

    // firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // views from xml
    ImageView admin_profile_image, admin_profile_cover_image;
    TextView admin_profile_name, admin_profile_email, admin_profile_phone;
    FloatingActionButton edit_profile_button;


    // progress dialog
    ProgressDialog progressDialog;

    // permissions
    private static final int CAMERA_REQUEST_CODE = 100;




    public AdminProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);


        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("adminUsers");


        // init views
        admin_profile_image = view.findViewById(R.id.profile_image);
        admin_profile_name = view.findViewById(R.id.admin_profile_name);
        admin_profile_email = view.findViewById(R.id.admin_profile_email);
        admin_profile_phone = view.findViewById(R.id.admin_profile_phone);
        admin_profile_cover_image = view.findViewById(R.id.admin_profile_cover_photo);
        edit_profile_button = view.findViewById(R.id.profile_edit_fab_button);


        // init progress dialog
        progressDialog = new ProgressDialog(getActivity());



        Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // check until required data found

                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    // get data
                    String name = ""+ ds.child("name").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String phone = ""+ ds.child("phone").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String cover = ""+ ds.child("cover").getValue();

                    // set data

                    admin_profile_email.setText(email);
                    admin_profile_name.setText(name);
                    admin_profile_phone.setText(phone);

                    try {
                        // if the image is available
                        Picasso.get().load(image).into(admin_profile_image);

                    }
                    catch (Exception e){
                        // if something went wrong use thr default image
                        Picasso.get().load(R.drawable.ic_action_face).into(admin_profile_image);
                    }


                    try {
                        // if the image is available
                        Picasso.get().load(cover).into(admin_profile_cover_image);

                    }
                    catch (Exception e){

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    edit_profile_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showEditProfileDialog();
        }
    });

        return view;
    }

    private void showEditProfileDialog() {


        // options to show in dialog
        String options [] = {"Edit profile picture", "Edit cover photo", "Edit name", "Edit phone"};

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Choose action");
        // set items to dialog

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // handle dialog items clicked
                if (which == 0){
                //Edit profile picture clicked

                    progressDialog.setMessage("updating profile picture");
                    showImagePicDialog();
                }
                else if (which == 1){
                    // Edit cover photo clicked
                    progressDialog.setMessage("updating cover picture");
                }
                else  if (which == 2){
                    // Edit name clicked
                    progressDialog.setMessage("updating name");
                }
                else if (which == 3){
                    // Edit phone clicked
                    progressDialog.setMessage("updating phone number");
                }

            }
        });

        // create and show dialog
        builder.create().show();

    }

    private void showImagePicDialog() {

     // show dialog options with camera and gallery

        // options to show in dialog
        String options [] = {"Camera", "Gallery"};

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Pick image from");
        // set items to dialog

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // handle dialog items clicked
                if (which == 0){
                    // camera clicked


                }
                else if (which == 1) {
                    // gallery clicked
                }

            }
        });

        // create and show dialog
        builder.create().show();


    }
}
