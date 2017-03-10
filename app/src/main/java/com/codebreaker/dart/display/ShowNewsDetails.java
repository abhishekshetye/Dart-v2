package com.codebreaker.dart.display;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.codebreaker.dart.R;

public class ShowNewsDetails extends AppCompatActivity {

    private TextView title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_details);

        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            title.setText(extras.getString("TITLE"));
            desc.setText(extras.getString("DESC"));
        }
    }
}
