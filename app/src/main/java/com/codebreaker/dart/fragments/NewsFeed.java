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

import com.codebreaker.dart.R;
import com.codebreaker.dart.adapters.NewsfeedAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    List<String> news = new ArrayList<>();

    private RecyclerView recyclerview;
    private NewsfeedAdapter adapter;

    public NewsFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        NetworkUrl("https://api.newsriver.io/v2/search?query=title%3A%22table%20tennis%22&sortBy=discoverDate&sortOrder=DESC&limit=15");

        recyclerview = (RecyclerView) v.findViewById(R.id.newsfeedrecycler);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new NewsfeedAdapter(getContext());
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);

        return v;
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
                news.add(obj.getString("title"));
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
