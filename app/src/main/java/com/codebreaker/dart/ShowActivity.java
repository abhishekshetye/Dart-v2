package com.codebreaker.dart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codebreaker.dart.customsearch.JsonTest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        updatePersonalInformation();

    }

    private void updatePersonalInformation() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(this, " " + user.getEmail(), Toast.LENGTH_SHORT).show();
        ref.child("users").child(user.getUid()).child("personal").child("name").setValue(user.getDisplayName());
    }

    public void custom(View v){
        startActivity(new Intent(this, JsonTest.class));
    }


    public void amazon(View v){
        startActivity(new Intent(this, AmazonProductAd.class));
    }
}
