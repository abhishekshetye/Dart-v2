package com.codebreaker.dart.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codebreaker.dart.R;
import com.codebreaker.dart.nlp.Extract;
import com.codebreaker.dart.nlp.ExtractHandler;
import com.codebreaker.dart.nlp.Keyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class KeywordExtract extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public KeywordExtract() {
        // Required empty public constructor
    }

    TextView tv;
    EditText ed;

    Extract e;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_keyword_extract, container, false);

        e = new Extract(getContext());

        tv = (TextView) v.findViewById(R.id.tv1);
        ed = (EditText) v.findViewById(R.id.ed1);
        Button b = (Button) v.findViewById(R.id.but1);
        Button b2 = (Button) v.findViewById(R.id.but2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtractHandler handler = new ExtractHandler();
                handler.checkAndUpload(getContext());
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List<String> p = e.extractKeyPhrase(ed.getText().toString());
                String msg = ed.getText().toString();
                Log.d("INTRST", "sent -> " + msg );
                List<String> p = e.extract(msg);
                List<String> ps = e.extractKeyPhrase(msg);
                //call my method
                ExtractHandler extractHandler = new ExtractHandler();
                extractHandler.extract(msg, getContext(), e);

                String dis = "";
                for(String s : p){
                    dis += s + " ";
                }
                dis += " | ";
                for(String s : ps){
                    dis += s + " ";
                }

                dis += " | ";
                try {
                    List<Keyword> kw = e.guessFromString(ed.getText().toString());
                    for(Keyword k : kw){
                        dis += k.getStem() + "-> " + k.getFrequency() + " ";
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                tv.setText(dis);
            }
        });

//        try {
//            Log.d("SLIMF", readFromAssets(getContext(), "tags.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("SLIMF", "file open exception");
//        }
        return v;
    }



}
