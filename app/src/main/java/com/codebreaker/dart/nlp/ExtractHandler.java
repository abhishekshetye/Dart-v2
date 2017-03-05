package com.codebreaker.dart.nlp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codebreaker.dart.database.DatabaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by abhishek on 3/5/17.
 */

public class ExtractHandler {


    private FirebaseAuth mAuth;

    public void extract(String message, Context context){

        Extract extract = new Extract();
        List<String> phrases = extract.extractKeyPhrase(message);

        DatabaseHandler handler = new DatabaseHandler(context);
        for(String phrase: phrases){
            handler.addInterest(phrase);
        }
        handler.close();
    }


    public void checkAndUpload(Context context){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference reference =  ref.child("users").child(user.getUid());

        DatabaseHandler handler = new DatabaseHandler(context);
        handler.checkAndUpdateInterest();
        List<String> phrases = handler.getInterestsToUpload();
        for(String interest: phrases){
            DatabaseReference newRef = reference.child("interests").push();
            newRef.setValue(interest);
        }
        handler.updateDBValue(phrases);
        handler.close();
        Log.d("SLIMF", "Completed");
    }

}
