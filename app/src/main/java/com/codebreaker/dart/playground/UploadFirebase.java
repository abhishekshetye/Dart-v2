package com.codebreaker.dart.playground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codebreaker.dart.R;
import com.codebreaker.dart.display.ChatMessage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UploadFirebase extends AppCompatActivity {


    private DatabaseReference reference;
    private String fruits[] = {"orange", "orange", "pineapple", "kiwi", "sugarcane", "pear"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_firebase);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//      working
//        reference = FirebaseDatabase.getInstance().getReference();
//
//        for(String fruit: fruits){
//            DatabaseReference newRef = reference.child("fruit").push();
//            newRef.setValue(fruit);
//        }

        ChatMessage message = new ChatMessage("message", false, true);

        FirebaseDatabase.getInstance().getReference("messages").setValue(message);

    }

    public void addPersonalInformation(List<String> interests){

        reference = FirebaseDatabase.getInstance().getReference();

        for(String interest: interests){
            DatabaseReference newRef = reference.child("personal").push();
            newRef.setValue(interest);
        }
    }

    public void addNewInterests(List<String> interests){

        reference = FirebaseDatabase.getInstance().getReference();

        for(String interest: interests){
            DatabaseReference newRef = reference.child("interests").push();
            newRef.setValue(interest);
        }
    }


}
