package com.codebreaker.dart.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebreaker.dart.News;
import com.codebreaker.dart.R;
import com.codebreaker.dart.adapters.NewsfeedAdapter;
import com.codebreaker.dart.database.DatabaseHandler;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeed extends Fragment {

    private final OkHttpClient client = new OkHttpClient();
    String NEWSFEED_API = "sBBqsGXiYgF0Db5OV5tAw7w2w2z2uV0kwr2rFM99s0IDMfrSG8OBGXeB2iH4XmiX";

    private RecyclerView recyclerview;
    private NewsfeedAdapter adapter;

    public NewsFeed() {
        // Required empty public constructor
    }

    Set<Integer> visited = new HashSet<>();
    String qry = "technology";
    List<News> news = new ArrayList<>();

    List<String> topics = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        //Remaining...
//        try {
//            NetworkUrl("https://api.newsriver.io/v2/search?query=text%3A" + URLEncoder.encode(qry, "UTF-8").replace("+", "%20")  + "&sortBy=discoverDate&sortOrder=DESC&limit=15");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        recyclerview = (RecyclerView) v.findViewById(R.id.newsfeedrecycler);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new NewsfeedAdapter(getContext());
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);

        getRandomInterests(); //test this

        return v;
    }

    public void getRandomInterests(){
        DatabaseHandler handler = new DatabaseHandler(getContext());
        List<String> interests = handler.getInterests();
        if(interests.size()==0){
            return;
        }
        for(int j = 0; j<interests.size() && j<3 ; j++) {

            int max = interests.size();
            int randomNum = ThreadLocalRandom.current().nextInt(0, max);
            while (visited.contains(randomNum)) {
                randomNum = ThreadLocalRandom.current().nextInt(0, max);
            }
            visited.add(randomNum);

            topics.add(interests.get(randomNum));
        }


        for(String topic: topics){
            try {
                NetworkUrl("https://api.newsriver.io/v2/search?query=text%3A" + URLEncoder.encode(topic, "UTF-8").replace("+", "%20")  + "&sortBy=discoverDate&sortOrder=DESC&limit=15");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private void NetworkUrl(String url){

        Request request = new Request.Builder()
                .addHeader("Authorization", NEWSFEED_API)
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                Log.d("NEWSFEED", res);
                parse(res);
            }

        });
    }


    private void parse(String data) {
        try {
            JSONArray arr = new JSONArray(data);

            for(int i=0; i<arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);

                String desc = obj.getString("text");
                String title = obj.getString("title");

                news.add(new News(title, desc));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        changeViews();
    }

    private void changeViews(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.getItems(news);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
