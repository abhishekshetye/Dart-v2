package com.codebreaker.dart;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyInstanceService extends FirebaseInstanceIdService {

    FirebaseAuth mAuth;

    public MyInstanceService() {

    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("NOTIF", "Refreshed token: " + refreshedToken);
        updatePersonalInformation(refreshedToken);
    }


    private void updatePersonalInformation(String refreshedToken) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ref.child("users").child(user.getUid()).child("personal").child("token").setValue(refreshedToken);
    }

}
