package com.codebreaker.dart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codebreaker.dart.amazon.AmazonProductAdvertisingApiRequestBuilder;
import com.codebreaker.dart.amazon.AmazonWebServiceAuthentication;
import com.codebreaker.dart.amazon.AmazonWebServiceLocation;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AmazonProductAd extends AppCompatActivity {

    String yourAccessKeyId = "AKIAJGRDZTPBKD2ATT7A";
    String yourSecretAccessKey = "4CSEVmjvmR7qWnxNeidHF/+QHkShE4MCvIccCTNQ";
    String yourAssociateTag = "abhishek96-21";
    TextView tv;
    boolean err = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amazon_product_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv = (TextView) findViewById(R.id.myid);

        AmazonWebServiceAuthentication myAuthentication = AmazonWebServiceAuthentication.create(yourAssociateTag, yourAccessKeyId, yourSecretAccessKey);
        final String requestUrl = AmazonProductAdvertisingApiRequestBuilder.forItemSearch("Deadpool Movie")
                .createRequestUrlFor(AmazonWebServiceLocation.IN, myAuthentication);
        Log.d("URL ", requestUrl);

        //new Network(requestUrl).execute();
        try {
            run(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
            Err();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    void Err(){
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    void Succ(){
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
    //https://drive.google.com/open?id=0B-RfMH1VQ3fsN19aTm44SjBYa3c
    private final OkHttpClient client = new OkHttpClient();

    public void run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AmazonProductAd.this, "Errror " + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                err = false;
                System.out.println(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("Success");
                    }
                });
            }


        });

    }



}
