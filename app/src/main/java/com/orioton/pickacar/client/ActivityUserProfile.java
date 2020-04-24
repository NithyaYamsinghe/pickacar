package com.orioton.pickacar.client;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.orioton.pickacar.R;

import java.io.IOException;

public class ActivityUserProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_PHOTO = 100;
    private Uri uriProfilePhoto;

    ImageView imgPofilePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imgPofilePhoto = findViewById(R.id.img_profile_photo);

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
}
