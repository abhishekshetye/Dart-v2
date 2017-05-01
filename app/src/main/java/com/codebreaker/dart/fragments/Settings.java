package com.codebreaker.dart.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codebreaker.dart.News;
import com.codebreaker.dart.R;
import com.codebreaker.dart.adapters.NewsfeedAdapter;
import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.display.ShowInterests;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {



    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView name = (TextView) v.findViewById(R.id.nameid);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        name.setText(user.getDisplayName());
        Button interests = (Button) v.findViewById(R.id.interests);

        Button opensource = (Button) v.findViewById(R.id.opensource);

        Button logout = (Button) v.findViewById(R.id.logout);

        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ShowInterests.class);
                startActivity(i);
            }
        });

        opensource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager.getInstance().logOut();
                mAuth.signOut();
                SharedPreferences prefs = getContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("TAG_LAUNCH", true);
                editor.commit();
                Snackbar.make(v, "Logging out..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ((Activity) getContext()).finish();

            }
        });

        return v;
    }


}
