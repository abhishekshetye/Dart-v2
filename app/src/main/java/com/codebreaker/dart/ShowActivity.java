package com.codebreaker.dart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.codebreaker.dart.customapi.JsonTest;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
    }

    public void custom(View v){
        startActivity(new Intent(this, JsonTest.class));
    }


    public void amazon(View v){
        startActivity(new Intent(this, AmazonProductAd.class));
    }
}
