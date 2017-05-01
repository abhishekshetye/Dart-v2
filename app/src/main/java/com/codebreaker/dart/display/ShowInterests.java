package com.codebreaker.dart.display;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codebreaker.dart.R;
import com.codebreaker.dart.adapters.ShowInterstsAdapter;
import com.codebreaker.dart.database.DatabaseHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowInterests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_interests);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.showrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ShowInterstsAdapter adapter = new ShowInterstsAdapter(this);
        recyclerView.setAdapter(adapter);

        //from firebase
        String key = getSharedPreferences("MYPREFS", MODE_PRIVATE).getString("MYKEY", "null");
        Log.d("IOI", key);
        if(!key.equalsIgnoreCase("null")){

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference newRef = database.child("users").child(key).child("interests").child("bigram");

            final Query myTopPostsQuery = newRef.orderByValue();
            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot video : dataSnapshot.getChildren()) {
                        Map<String, String> mapObj = (Map<String, String>) video.getValue();
                        Log.d("FIR", mapObj.toString());
                        //videos.add(new Video((String) mapObj.get("name"), (String) mapObj.get("url")));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }

        //get interests
        DatabaseHandler handler = new DatabaseHandler(this);
        List<String> intr = handler.getInterests();
        Log.d("SLIMF", "count " + intr.size());
        adapter.setItems(intr);
        adapter.notifyDataSetChanged();
    }
}
