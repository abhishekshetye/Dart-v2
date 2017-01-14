package com.codebreaker.dart;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by abhishek on 1/8/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
