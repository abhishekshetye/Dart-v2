package com.codebreaker.dart.nlp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.database.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * Created by abhishek on 3/5/17.
 */

public class ExtractHandler {


    private FirebaseAuth mAuth;

    

    public void extract(String message, Context context, Extract extract){

        List<String> phrases = extract.extract(message);

        DatabaseHandler handler = new DatabaseHandler(context);
        for(String phrase: phrases){
            handler.addInterest(phrase);
        }
        List<Message> d = handler.getAllData();
        for(Message m : d){
            Log.d("SLIMF", m.getMessage());
        }
        handler.close();
    }


    public void checkAndUpload(Context context){
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //if internet is not available, dont run this method.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference reference =  ref.child("users").child(user.getUid());

        DatabaseHandler handler = new DatabaseHandler(context);
        handler.checkAndUpdateInterest();
        List<String> phrases = handler.getInterestsToUpload();
        if(phrases.size() == 0)
            Log.d("SLIMF", "None to upload yet");
        for(String s: phrases){
            Log.d("SLIMF", "to upload -> " + s );
        }
        for(String interest: phrases){
            DatabaseReference newRef = reference.child("interests").child("bigram").push();
            newRef.setValue(interest);
        }
        handler.updateDBValue(phrases);
        handler.close();
        Log.d("SLIMF", "Completed");
    }

}
