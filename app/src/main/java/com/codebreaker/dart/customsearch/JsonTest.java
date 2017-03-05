package com.codebreaker.dart.customsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codebreaker.dart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abhishek on 1/27/17.
 */


public class JsonTest extends AppCompatActivity {
    String key = "AIzaSyCDAZMzd_RMQXvQTpq31nJcNqpLPo8Oz10";
    String qry = "";
    boolean err = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);
        Button b = (Button) findViewById(R.id.button2);
        final EditText e = (EditText) findViewById(R.id.editText);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Editable qry = e.getText();
                    run("https://www.googleapis.com/customsearch/v1?key=AIzaSyCDAZMzd_RMQXvQTpq31nJcNqpLPo8Oz10&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + "&alt=json");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });


    }
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
                        Toast.makeText(getBaseContext(), "Errror " + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                err = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String res=response.body().string();
                            JSONObject jObject = new JSONObject(res);
                            JSONArray jArray = jObject.getJSONArray("items");
                            JSONObject oneObject = jArray.getJSONObject(0);
                            String title = oneObject.getString("title");
                            String link = oneObject.getString("link");
                            String snippet = oneObject.getString("snippet");
                            final TextView t1 = (TextView) findViewById(R.id.textView);
                            final TextView t2 = (TextView) findViewById(R.id.textView2);
                            final TextView t3 = (TextView) findViewById(R.id.textView3);

                            t1.setText(title);
                            t2.setText(link);
                            t3.setText(snippet);



                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        });

    }

}

