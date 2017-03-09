package com.codebreaker.dart;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by abhishek on 1/8/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //did not test yet.. 9:52 9/3/2017
    }
}
