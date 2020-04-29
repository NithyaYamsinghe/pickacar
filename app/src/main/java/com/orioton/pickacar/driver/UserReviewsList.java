package com.orioton.pickacar.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.orioton.pickacar.R;

public class UserReviewsList extends AppCompatActivity {

    Button addReviewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews_list);
    }

    public void showAddReview(View view) {
        Intent intent = new Intent(this, AddReview.class);
        startActivity(intent);
    }
}
