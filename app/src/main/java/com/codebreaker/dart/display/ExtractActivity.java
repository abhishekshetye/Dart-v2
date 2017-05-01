package com.codebreaker.dart.display;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codebreaker.dart.R;
import com.codebreaker.dart.nlp.Extract;
import com.codebreaker.dart.nlp.ExtractHandler;
import com.codebreaker.dart.nlp.Keyword;

import java.io.IOException;
import java.util.List;

public class ExtractActivity extends AppCompatActivity {
    TextView tv;
    EditText ed;
    Extract e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keyword_extract);
        e = new Extract(this);

        tv = (TextView) findViewById(R.id.tv1);
        ed = (EditText) findViewById(R.id.ed1);
        Button b = (Button) findViewById(R.id.but1);
        Button b2 = (Button) findViewById(R.id.but2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtractHandler handler = new ExtractHandler();
                handler.checkAndUpload(ExtractActivity.this);
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
                extractHandler.extract(msg, ExtractActivity.this, e);

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
    }
}
