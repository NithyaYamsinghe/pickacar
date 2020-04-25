package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orioton.pickacar.R;
import com.orioton.pickacar.model.CarUploadInfo;

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

                // call method to upload data to firebase
                uploadDataToFirebase();

            }
        });

        // assign firebase storage instance to firebase storage reference object
        storageReference = FirebaseStorage.getInstance().getReference();

        // assign firebase database instance to firebase storage database object
        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        // progress dialog
        progressDialog = new ProgressDialog(AddNewCarActivity.this);


    }

    private void uploadDataToFirebase() {

        // check whether file path uri empty or not
        if (filePathUri != null){

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
                    while (!urlTask.isSuccessful());
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
                    CarUploadInfo carUploadInfo = new CarUploadInfo(addBrand, addColor, addCondition,addDescription, downloadUrl.toString(),
                            addModel, addPassengers, addReleasedYear, addModel.toLowerCase());

                    // getting image upload id
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(carUploadInfo);




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
        }
        else {
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

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK  && data != null && data.getData() != null){
            filePathUri = data.getData();
            try {
                // getting selected image in to bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);

                // setting bitmap in to image view
                image.setImageBitmap(bitmap);
            }
            catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
