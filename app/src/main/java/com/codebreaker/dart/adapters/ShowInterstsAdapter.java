package com.codebreaker.dart.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebreaker.dart.News;
import com.codebreaker.dart.R;
import com.codebreaker.dart.display.ShowNewsDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 3/5/17.
 */

public class ShowInterstsAdapter extends RecyclerView.Adapter<ShowInterstsAdapter.MyViewHolder> {

    private Context context;
    private List<String> news;

    public ShowInterstsAdapter(Context context){
        this.context = context;
        news = new ArrayList<>();
        Log.d("SLIMF", "called concs");
    }

    public void setItems(List<String> news){
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
        Log.d("SLIMF", "in " + news.get(position));
        holder.title.setText(news.get(position));
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
//                    Intent i = new Intent(context, ShowNewsDetails.class);
//                    i.putExtra("TITLE", news.get(getAdapterPosition()).getTitle());
//                    i.putExtra("DESC", news.get(getAdapterPosition()).getDescription());
//                    context.startActivity(i);
                }
            });
        }


    }
}
