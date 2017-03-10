package com.codebreaker.dart.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebreaker.dart.News;
import com.codebreaker.dart.R;
import com.codebreaker.dart.display.ShowNewsDetails;
import com.codebreaker.dart.fragments.NewsFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 3/5/17.
 */

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.MyViewHolder> {

    private Context context;
    private List<News> news;

    public NewsfeedAdapter(Context context){
        this.context = context;
        news = new ArrayList<>();
    }

    public void getItems(List<News> news){
        this.news = news;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(news.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            card = (CardView) itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowNewsDetails.class);
                    i.putExtra("TITLE", news.get(getAdapterPosition()).getTitle());
                    i.putExtra("DESC", news.get(getAdapterPosition()).getDescription());
                    context.startActivity(i);
                }
            });
        }


    }
}
