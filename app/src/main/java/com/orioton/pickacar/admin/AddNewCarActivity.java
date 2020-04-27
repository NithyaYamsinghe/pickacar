package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.model.CarUploadInfo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddNewCarActivity extends AppCompatActivity {

    EditText brand, model, color, releasedYear, passengers, description, condition;
    ImageView image;
    Button addButton;


    // folder path for firebase storage
    String storagePath = "Car_Image_Uploads/";

    // root database name for firebase database
    String databasePath = "cars";

    // creating uri
    Uri filePathUri;


    // creating storage reference and database reference
    StorageReference storageReference;
    DatabaseReference databaseReference;


    // progress dialog
    ProgressDialog progressDialog;

    // image request code for choosing image
    int IMAGE_REQUEST_CODE = 5;


    // intent data will be stored here
    String brandEdit, modelEdit, colorEdit, releasedYearEdit, passengerEdit, descriptionEdit, conditionEdit, imageEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);

        // action bar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add new car");


        // initializing
        brand = findViewById(R.id.add_car_brand);
        model = findViewById(R.id.add_car_model);
        color = findViewById(R.id.add_car_color);
        releasedYear = findViewById(R.id.add_car_released_year);
        passengers = findViewById(R.id.add_car_passengers);
        description = findViewById(R.id.add_car_description);
        condition = findViewById(R.id.add_car_condition);
        image = findViewById(R.id.add_car_image);
        addButton = findViewById(R.id.add_car_button);


        // get data from intent if not null
        Bundle intent = getIntent().getExtras();
        if (intent != null) {

            brandEdit = intent.getString("brand");
            modelEdit = intent.getString("model");
            colorEdit = intent.getString("color");
            releasedYearEdit = intent.getString("releasedYear");
            passengerEdit = intent.getString("passengers");
            descriptionEdit = intent.getString("description");
            conditionEdit = intent.getString("condition");
            imageEdit = intent.getString("image");

            // set these data to views
            brand.setText(brandEdit);
            model.setText(modelEdit);
            color.setText(colorEdit);
            releasedYear.setText(releasedYearEdit);
            passengers.setText(passengerEdit);
            description.setText(descriptionEdit);
            condition.setText(conditionEdit);
            Picasso.get().load(imageEdit).into(image);


            // change action bar and the button
            actionBar.setTitle("Update car");
            addButton.setText("Update");


        }


        // image click to choose image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // creating intent
                Intent intent = new Intent();

                // setting select image from phone storage
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), IMAGE_REQUEST_CODE);


            }
        });

        // button click to upload data to firebase
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addButton.getText().equals("Add Car")) {


                    // call method to upload data to firebase
                    uploadDataToFirebase();

                } else {

                    updateData();

                }


            }
        });

        // assign firebase storage instance to firebase storage reference object
        storageReference = FirebaseStorage.getInstance().getReference();

        // assign firebase database instance to firebase storage database object
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        // progress dialog
        progressDialog = new ProgressDialog(AddNewCarActivity.this);


    }

    private void updateData() {

        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // delete previous image
        deletePreviousImage();


    }

    private void deletePreviousImage() {


        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageEdit);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // deleted

                Toast.makeText(AddNewCarActivity.this, "previous image is deleted", Toast.LENGTH_SHORT).show();

                // upload new image
                uploadNewImage();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // failed
                Toast.makeText(AddNewCarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });
    }

    private void uploadNewImage() {

        // image_name
        String image_name = System.currentTimeMillis() + ".png";
        StorageReference storageReferenceNew = storageReference.child(storagePath + image_name);
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStreamNew = new ByteArrayOutputStream();

        // compress image
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStreamNew);
        byte[] data = byteArrayOutputStreamNew.toByteArray();
        UploadTask uploadTask = storageReferenceNew.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // uploaded
                Toast.makeText(AddNewCarActivity.this, "new image uploaded", Toast.LENGTH_SHORT).show();

                // get url of newly uploaded image
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri downloadUrl = urlTask.getResult();

                // update data with newest data
                updateDatabase(downloadUrl.toString());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // error
                Toast.makeText(AddNewCarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

    }

    private void updateDatabase(final String toString) {


        // new values to update

        final String addBrand = brand.getText().toString().trim();
        final String addModel = model.getText().toString().trim();
        final String addColor = color.getText().toString().trim();
        final String addReleasedYear = releasedYear.getText().toString().trim();
        final String addPassengers = passengers.getText().toString().trim();
        final String addCondition = condition.getText().toString().trim();
        final String addDescription = description.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceNew = firebaseDatabase.getReference("cars");

        Query query = databaseReferenceNew.orderByChild("image").equalTo(imageEdit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ds.getRef().child("brand").setValue(addBrand);
                    ds.getRef().child("color").setValue(addColor);
                    ds.getRef().child("condition").setValue(addCondition);
                    ds.getRef().child("model").setValue(addModel);
                    ds.getRef().child("search").setValue(addModel.toLowerCase());
                    ds.getRef().child("releasedYear").setValue(addReleasedYear);
                    ds.getRef().child("passengers").setValue(addPassengers);
                    ds.getRef().child("description").setValue(addDescription);

                    ds.getRef().child("image").setValue(toString);

                }
                progressDialog.dismiss();

                // start car list after update
                Toast.makeText(AddNewCarActivity.this, "database uploaded successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNewCarActivity.this, CarListActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadDataToFirebase() {

        // check whether file path uri empty or not
        if (filePathUri != null) {

            // setting progress bar title
            progressDialog.setTitle("uploading...");

            // show progress dialog
            progressDialog.show();

            // create second storage reference
            StorageReference storageReferenceSecond = storageReference.child(storagePath + System.currentTimeMillis() + "." + getFileExtension(filePathUri));

            // adding on success to storageReferenceSecond
            storageReferenceSecond.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    String addBrand = brand.getText().toString().trim();
                    String addModel = model.getText().toString().trim();
                    String addColor = color.getText().toString().trim();
                    String addReleasedYear = releasedYear.getText().toString().trim();
                    String addPassengers = passengers.getText().toString().trim();
                    String addCondition = condition.getText().toString().trim();
                    String addDescription = description.getText().toString().trim();

                    // hide progress
                    progressDialog.dismiss();

                    // show the toast that image is uploaded 
                    Toast.makeText(AddNewCarActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                    CarUploadInfo carUploadInfo = new CarUploadInfo(addBrand, addColor, addCondition, addDescription, downloadUrl.toString(),
                            addModel, addPassengers, addReleasedYear, addModel.toLowerCase());

                    // getting image upload id
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(carUploadInfo);
                    Intent intent = new Intent(AddNewCarActivity.this, CarListActivity.class);
                    startActivity(intent);


                }

            })
                    // if something went wrong
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // hide progress dialog 
                            progressDialog.dismiss();

                            // show error toast
                            Toast.makeText(AddNewCarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.setTitle("uploading...");

                        }
                    });
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    // method to get extension of selected image from file path uri
    private String getFileExtension(Uri filePathUri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // return the file extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filePathUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();
            try {
                // getting selected image in to bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);

                // setting bitmap in to image view
                image.setImageBitmap(bitmap);
            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
