package com.codebreaker.dart.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codebreaker.dart.R;
import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.database.Message;

import java.util.List;


public class TagSelector extends AppCompatActivity {

    int sci = 0, andr = 0, tech =0 , poli = 0, ios= 0, biz = 0, spor = 0;

    Button bsci, btech, bandr, bios, bpoli, bspor, bbiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bsci = (Button) findViewById(R.id.bsci);
        btech = (Button) findViewById(R.id.btech);
        bandr= (Button) findViewById(R.id.bandr);
        bios = (Button) findViewById(R.id.bios);
        bpoli = (Button) findViewById(R.id.bpoli);
        bspor= (Button) findViewById(R.id.bsports);
        bbiz = (Button) findViewById(R.id.bbiz);

        bandr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(bandr, andr);
                andr++;
            }
        });

        btech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(btech, tech);
                tech++;
            }
        });

        bpoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(bpoli, poli);
                poli++;
            }
        });

        bspor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(bspor, spor);
                spor++;
            }
        });

        bbiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(bbiz, biz);
                biz++;
            }
        });

        bios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp(bios, ios);
                ios++;
            }
        });

        //  int sci = 1, andr = 1, tech =1 , poli = 1, ios= 1 , biz = 1, spor = 1;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(TagSelector.this, "Setting up the environment", Toast.LENGTH_SHORT).show();
                DatabaseHandler handler = new DatabaseHandler(TagSelector.this);

                if(sci%2==1){
                    handler.addInterest("science", -1);
                }
                if(andr%2==1){
                    handler.addInterest("android", -1);
                }
                if(tech%2==1){
                    handler.addInterest("technology", -1);
                }
                if(poli%2==1){
                    handler.addInterest("politics", -1);
                }
                if(ios%2==1){
                    handler.addInterest("iOS", -1);
                }
                if(biz%2==1){
                    handler.addInterest("business", -1);
                }
                if(spor%2==1){
                    handler.addInterest("sports", -1);
                }

                List<Message> d = handler.getAllData();
                for(Message m : d){
                    Log.d("SLIMF", m.getMessage());
                }
                handler.close();

                Snackbar.make(view, "Going ahead...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent i = new Intent(TagSelector.this, ShowActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    public void disp(Button b, int counter){
        if(counter % 2 == 1){
            b.setBackgroundColor(getResources().getColor(R.color.btn_press));
        }else{
            b.setBackgroundColor(getResources().getColor(R.color.btn_normal));
        }
    }

    public void science(View v){
        if(sci % 2 == 1){
            bsci.setBackgroundColor(getResources().getColor(R.color.btn_press));
        }else{
            bsci.setBackgroundColor(getResources().getColor(R.color.btn_normal));
        }
        sci++;
    }

}
