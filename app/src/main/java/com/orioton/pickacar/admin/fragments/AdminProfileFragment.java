package com.orioton.pickacar.admin.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orioton.pickacar.MainActivity;
import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.AdminAddNewCarActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminProfileFragment extends Fragment {

    // firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    // storage
    StorageReference storageReference;


    // path to store user profile image and cover photo
    String storagePath = "Admin_Users_Profile_Cover_Images/";

    // views from xml
    ImageView admin_profile_image, admin_profile_cover_image;
    TextView admin_profile_name, admin_profile_email, admin_profile_phone;
    FloatingActionButton edit_profile_button;
    Button admin_delete_profile;


    // progress dialog
    ProgressDialog progressDialog;

    // to check profile or cover photo
    String profileOrCoverPhoto;

    // permissions
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    // uri of picked image
    Uri image_uri;


    // arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

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
        storageReference = getInstance().getReference();


        // init views
        admin_profile_image = view.findViewById(R.id.profile_image);
        admin_profile_name = view.findViewById(R.id.admin_profile_name);
        admin_profile_email = view.findViewById(R.id.admin_profile_email);
        admin_profile_phone = view.findViewById(R.id.admin_profile_phone);
        admin_profile_cover_image = view.findViewById(R.id.admin_profile_cover_photo);
        edit_profile_button = view.findViewById(R.id.profile_edit_fab_button);
        admin_delete_profile = view.findViewById(R.id.delete_profile);

        // init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // init progress dialog
        progressDialog = new ProgressDialog(getActivity());


        Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // check until required data found

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();

                    // set data

                    admin_profile_email.setText(email);
                    admin_profile_name.setText(name);
                    admin_profile_phone.setText(phone);

                    try {
                        // if the image is available
                        Picasso.get().load(image).into(admin_profile_image);

                    } catch (Exception e) {
                        // if something went wrong use thr default image
                        Picasso.get().load(R.drawable.ic_action_face).into(admin_profile_image);
                    }


                    try {
                        // if the image is available
                        Picasso.get().load(cover).into(admin_profile_cover_image);

                    } catch (Exception e) {

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

        admin_delete_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getActivity(), "successfully deleted user", Toast.LENGTH_SHORT).show();

                    }
                });

                Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                            firebaseAuth.signOut();
                            checkUserStatus();


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        return view;
    }


    private boolean checkStoragePermissions() {

        // check if storage permissions enabled or not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result;


    }

    private void requestStoragePermission() {

        // request run time storage permissions
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermissions() {

        // check if storage permissions enabled or not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result && result1;


    }

    private void requestCameraPermission() {

        // request run time storage permissions
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);

    }

    private void showEditProfileDialog() {


        // options to show in dialog
        String options[] = {"Edit profile picture", "Edit cover photo", "Edit name", "Edit phone", "Change password"};

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Choose action");
        // set items to dialog

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // handle dialog items clicked
                if (which == 0) {
                    //Edit profile picture clicked

                    progressDialog.setMessage("updating profile picture");
                    profileOrCoverPhoto = "image";
                    showImagePicDialog();
                } else if (which == 1) {
                    // Edit cover photo clicked
                    progressDialog.setMessage("updating cover picture");
                    profileOrCoverPhoto = "cover";
                    showImagePicDialog();
                } else if (which == 2) {
                    // Edit name clicked
                    progressDialog.setMessage("updating name");
                    showNamePhoneUpdateDialog("name");
                } else if (which == 3) {
                    // Edit phone clicked
                    progressDialog.setMessage("updating phone number");
                    showNamePhoneUpdateDialog("phone");
                }
             else if (which == 4) {
                // Change phone clicked
                progressDialog.setMessage("updating password");
               showChangePasswordDialog();
            }

            }
        });

        // create and show dialog
        builder.create().show();

    }

    private void showChangePasswordDialog() {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.admin_update_password_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText password = view.findViewById(R.id.admin_change_edit_Password);
        final EditText confirmPassword = view.findViewById(R.id.admin_change_edit_cPassword);
        final Button changePasswordButton = view.findViewById(R.id.admin_update_password);


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate data

                String oldPassword = password.getText().toString().trim();
                String newPassword = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPassword)){

                    Toast.makeText(getActivity(), "Enter you current password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.length() < 6){

                    Toast.makeText(getActivity(), "Password length mut  be at least 7 characters", Toast.LENGTH_SHORT).show();


                    return;
                }


                dialog.dismiss();
                updatePassword(oldPassword, newPassword);
            }
        });



    }

    private void updatePassword(String oldPassword, final String newPassword) {

        progressDialog.show();

        // get current user
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                currentUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void showNamePhoneUpdateDialog(final String keyValue) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + keyValue);

        // layout of dialog


        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        // add edit text
        final EditText editText = new EditText(getActivity());

        editText.setHint("Enter" + keyValue);
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        // add buttons
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // input value
                String value = editText.getText().toString().trim();
                // validate
                if (!TextUtils.isEmpty(value)) {
                    progressDialog.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(keyValue, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Please enter " + keyValue, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void showImagePicDialog() {

        // show dialog options with camera and gallery

        // options to show in dialog
        String options[] = {"Camera", "Gallery"};

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle("Pick image from");
        // set items to dialog

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // handle dialog items clicked
                if (which == 0) {
                    // camera clicked
                    if (!checkCameraPermissions()) {

                        requestCameraPermission();

                    } else {
                        pickFromCamera();
                    }


                } else if (which == 1) {
                    // gallery clicked


                    if (!checkStoragePermissions()) {
                        requestStoragePermission();
                    } else {

                        pickFromGallery();
                    }
                }

            }
        });

        // create and show dialog
        builder.create().show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // method call when user gives permissions
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {

                // picking from camera

                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted) {
                        // permissions enabled
                        pickFromCamera();
                    } else {

                        // permission denied 
                        Toast.makeText(getActivity(), "Please enable camera & storage permissions", Toast.LENGTH_SHORT).show();

                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted) {
                        // permissions enabled
                        pickFromGallery();
                    } else {

                        // permission denied
                        Toast.makeText(getActivity(), "Please enable  storage permissions", Toast.LENGTH_SHORT).show();

                    }
                }


            }
            break;


        }

    }

    private void pickFromCamera() {

        // intent of picking image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");

        // put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        // intent to start camera

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }

    private void pickFromGallery() {


        // pick image from gallery


        Intent galleryIntent = new Intent(Intent.ACTION_PICK);

        galleryIntent.setType("image/*");


        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // this will call after picking image from gallery
        if (resultCode == RESULT_OK) {


            if (requestCode == IMAGE_PICK_GALLERY_CODE) {


                // image is picked from gallery
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                // image picked from camera
                uploadProfileCoverPhoto(image_uri);


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {


        progressDialog.show();

        // path and name of the image to be stored
        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "_" + user.getUid();

        StorageReference storageReferenceNew = storageReference.child(filePathAndName);
        storageReferenceNew.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // image is uploaded to storage get url and store in database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        // check image uploaded or not
                        if (uriTask.isSuccessful()) {
                            // image uploaded

                            // add url to database


                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto, downloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Image updated successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Error updating image..", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else {

                            // error
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "error occurred", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // when something went wrong
                progressDialog.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();

        } else if (id == R.id.action_add) {
            startActivity(new Intent(getActivity(), AdminAddNewCarActivity.class));


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
