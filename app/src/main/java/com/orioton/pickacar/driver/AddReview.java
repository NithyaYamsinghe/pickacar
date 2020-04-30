package com.orioton.pickacar.driver;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orioton.pickacar.R;
import com.orioton.pickacar.client.ActivityLogIn;
import com.orioton.pickacar.driver.model.ReviewModel;

public class AddReview extends AppCompatActivity {

    private EditText title,review;
    private Button yourReview;

    private Firebase root;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AwesomeValidation awesomeValidation;

    String username, driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        mAuth = FirebaseAuth.getInstance();

        root = new Firebase("https://pickacar-b3bf4.firebaseio.com/reviews");

        // initializing validation style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        title = (EditText) findViewById(R.id.short_review_input);
        review = (EditText) findViewById(R.id.review_input);
        yourReview = (Button) findViewById(R.id.add_review_button);



        // applying the validations
        awesomeValidation.addValidation(this, R.id.short_review_input,
                ".{5,}", R.string.too_short);

        awesomeValidation.addValidation(this, R.id.review_input,
                ".{10,}", R.string.too_short);

        yourReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });
    }



    private void addReview() {
        if (awesomeValidation.validate()) {

            Bundle intent = getIntent().getExtras();
            if (intent != null) {

                username = intent.getString("Name");
            }

            // sign the user up if the validation passed
            final String value1 = username;
            FirebaseUser user = mAuth.getCurrentUser();
            String value2 = user.getEmail();
            final String value3 = title.getText().toString().trim();
            final String value4 = review.getText().toString().trim();


//           String value1 = "title.getText().toString().trim()";
//           String value2 = "review.getText().toString().trim()";
//           String value3 = "username";
//           String value4 = "username";




                        // add user to the database
//                        String userId = mAuth.getCurrentUser().getUid();
                        ReviewModel reviews = new ReviewModel(value1,value2,value3,value4);
                        root.push().setValue(reviews);

                        Toast.makeText(getApplicationContext(), "Your Review has been Saved!", Toast.LENGTH_SHORT).show();


        } else {
            // validation failed
            Toast.makeText(getApplicationContext(), "Please check for the errors", Toast.LENGTH_SHORT).show();
        }

    }
}

