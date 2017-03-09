package com.codebreaker.dart.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codebreaker.dart.R;
import com.codebreaker.dart.display.ChatMessage;

import java.util.List;

/**
 * Created by abhishek on 1/14/17.
 */

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;
    private Context context;
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.item_mine_message, data);
        this.context = context;
    }
    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);
        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return MY_IMAGE;
        else return OTHER_IMAGE;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == MY_IMAGE) {
            Log.d("IMAGED", "Here 1");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageDrawable(getItem(position).getImagesource());
        } else {
            Log.d("IMAGED", "Here 4");
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_image, parent, false);
             ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            Glide.with(context).load(getItem(position).getContent()).into(imageView);
             //imageView.setImageDrawable(getItem(position).getImagesource());

        }
        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage msg = getItem(position);
                String dl = msg.getDeeplink();
                if(dl!=null){
                    String url = dl;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            }
        });
        return convertView;
    }
}