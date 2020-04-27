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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    Button btnProfileUpdate;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String databasePath = "users";
    private String currentUserId;
    private String profilePhotoUrl = "https://clikiads.com/static/images/blank_profile.png";
    private String userId;

    AwesomeValidation awesomeValidation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressDialog = new ProgressDialog(ActivityUserProfile.this);

        // showing the progress dialog
        progressDialog.setTitle("Getting profile...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        // getting the current userId
        currentUserId = mAuth.getCurrentUser().getUid();

        imgPofilePhoto = findViewById(R.id.img_profile_photo);
        profileName = findViewById(R.id.tv_profile_name);
        userName = findViewById(R.id.tv_name);
        userEmail = findViewById(R.id.tv_email);
        userPhone = findViewById(R.id.tv_phone);
        btnProfileUpdate = findViewById(R.id.btn_update_profile);

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // applying the validations
        awesomeValidation.addValidation(this, R.id.tv_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.tv_name,
                ".{4,}", R.string.invalid_name);

        awesomeValidation.addValidation(this, R.id.tv_phone,
                ".{10,}", R.string.invalid_name);

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                    User user = usersSnapshot.getValue(User.class);
                    // getting the current user info from the db snapshot
                    if (user.getUserId().equals(currentUserId)) {

                        // assigning the user id
                        userId = usersSnapshot.getKey();

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
        btnProfileUpdate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.img_profile_photo):
                // choosing the profile photo to upload
                photoChooser();
                break;
            case (R.id.btn_update_profile):
                // update profile
                updateProfile();
                break;
        }
    }

    private void updateProfile() {
        if (awesomeValidation.validate()) {

            progressDialog.setTitle("Updating profile...");
            progressDialog.show();

            // update the user in the database
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            String userNameVal = userName.getText().toString().trim();
            String userEmailVal = userEmail.getText().toString().trim();
            String userPhoneVal = userPhone.getText().toString().trim();

            User user = new User(currentUserId, userNameVal, userEmailVal, userPhoneVal, profilePhotoUrl);
            databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Input valid data!", Toast.LENGTH_SHORT).show();
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
                // upload the selected profile photo to Firebase
                uploadProfilePhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void uploadProfilePhoto() {
        // upload profile photo
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("users/" + System.currentTimeMillis() + ".jpg");

        // check for the existence of a new profile photo
        if (uriProfilePhoto != null) {
            progressDialog.setTitle("Uploading profile photo...");
            progressDialog.show();

            storageReference.putFile(uriProfilePhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            profilePhotoUrl = uri.toString();
                        }
                    });
                }
            });

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
