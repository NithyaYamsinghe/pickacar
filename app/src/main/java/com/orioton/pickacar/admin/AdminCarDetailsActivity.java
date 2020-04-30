package com.orioton.pickacar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orioton.pickacar.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdminCarDetailsActivity extends AppCompatActivity {

    TextView brand, model, color, releasedYear, passenger, description, condition;
    ImageView image;
    Button  saveButton, shareButton, setButton;
    Bitmap bitmap;
    private  static  final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_car_details);
        // action bar
        ActionBar actionBar = getSupportActionBar();

        // action bar title
        actionBar.setTitle("Car Details");

        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // initialize views
        brand = findViewById(R.id.car_detail_brand_txt);
        model = findViewById(R.id.car_detail_model_txt);
        color = findViewById(R.id.car_detail_color_txt);
        releasedYear = findViewById(R.id.car_detail_year_txt);
        passenger = findViewById(R.id.car_detail_passengers_txt);
        description = findViewById(R.id.car_detail_description_txt);
        condition = findViewById(R.id.car_detail_condition_txt);
        image = findViewById(R.id.car_detail_image);
        saveButton = findViewById(R.id.car_detail_save_button);
        shareButton = findViewById(R.id.car_detail_share_button);
        setButton = findViewById(R.id.car_detail_set_button);




        // save button click handler
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if os >= marshmallow grant run time permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        // show popup to grant permission
                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);

                    }
                    else {
                        // permission already granted
                        saveImage();
                    }
                }
                else {
                    // system os < marshmallow
                    saveImage();

                }

            }
        });

        // share button click handler
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImage();

            }
        });

        // set button click handler
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setImageWallpaper();

            }
        });



        // get data from intent

        String images = getIntent().getStringExtra("image");
        String models = getIntent().getStringExtra("model");
        String brands = getIntent().getStringExtra("brand");
        String colors = getIntent().getStringExtra("color");
        String releasedYears = getIntent().getStringExtra("releasedYear");
        String passengers = getIntent().getStringExtra("passengers");
        String descriptions = getIntent().getStringExtra("description");
        String conditions = getIntent().getStringExtra("condition");


        // set data to views
        brand.setText(brands);
        model.setText(models);
        color.setText(colors);
        releasedYear.setText(releasedYears);
        passenger.setText(passengers);
        description.setText(descriptions);
        condition.setText(conditions);
        Picasso.get().load(images).into(image);



    }

    // set wallpaper method
    private void setImageWallpaper() {


        // get image from image view as bitmap
        bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try{

            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "wallpaper changed", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void shareImage() {

        try {


            // get image from image view as bitmap
            bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            String s = brand.getText().toString() + "\n" + description.getText().toString();
            File file = new File(getExternalCacheDir(),"sample.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true, false);

            // intent to share image and text
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, s);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share via"));


        }
        catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void saveImage() {


        // get image from image view as bitmap
        bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

        // time stamp for image name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        // path to external storage
        File path = Environment.getExternalStorageDirectory();

        // create folder name firebase
        File dir = new File(path + "/Firebase/");
        dir.mkdirs();


        // image name
        String imageName = timestamp + ".PNG";
        File file = new File(dir, imageName);

        OutputStream outputStream;

        try {

            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, imageName + " saved to " + dir, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            // failed saving image

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case WRITE_EXTERNAL_STORAGE_CODE:
                // if request code cancelled result array will be empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted save image
                    saveImage();
                }
                else {
                    Toast.makeText(this, "enable permission to save image", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // handle on back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
