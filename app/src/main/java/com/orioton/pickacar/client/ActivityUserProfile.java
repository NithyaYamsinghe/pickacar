package com.orioton.pickacar.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.AddNewCarActivity;
import com.orioton.pickacar.client.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivityUserProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_PHOTO = 100;
    private Uri uriProfilePhoto;

    ImageView imgPofilePhoto;
    TextView profileName;
    TextView userEmail;
    TextView userPhone;
    TextView userName;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String databasePath = "users";
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressDialog = new ProgressDialog(ActivityUserProfile.this);

        // showing the progress dialog
        progressDialog.setTitle("Getting Profile...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        // getting the current userId
        currentUserId = mAuth.getCurrentUser().getUid();

        imgPofilePhoto = findViewById(R.id.img_profile_photo);
        profileName = findViewById(R.id.tv_profile_name);
        userName = findViewById(R.id.tv_name);
        userEmail = findViewById(R.id.tv_email);
        userPhone = findViewById(R.id.tv_phone);

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                    User user = usersSnapshot.getValue(User.class);

                    // getting the current user info from the db snapshot
                    if (user.getUserId().equals(currentUserId)) {
                        // showing user profile
                        showUserProfile(user);

                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imgPofilePhoto.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.img_profile_photo):
                // choosing the profile photo to upload
                photoChooser();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfilePhoto = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilePhoto);
                imgPofilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void photoChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile photo to upload"), CHOOSE_PHOTO);
    }

    private void showUserProfile(User user) {
        profileName.setText(user.getUserName());
        userName.setText(user.getUserName());
        userPhone.setText(user.getUserPhone());
        userEmail.setText(user.getUserEmail());

        // loading the profile photo
        Picasso.get().load(user.getUserProfilePhotoUrl()).into(imgPofilePhoto);

        progressDialog.dismiss();

    }
}
