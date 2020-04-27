package com.orioton.pickacar.driver;

import android.app.Application;

import com.firebase.client.Firebase;

public class FirebaseCon extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
