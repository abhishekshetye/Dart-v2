package com.codebreaker.dart.fragments;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.codebreaker.dart.R;
import com.codebreaker.dart.adapters.ChatMessageAdapter;
import com.codebreaker.dart.amazon.AmazonHelper;
import com.codebreaker.dart.amazon.AmazonListener;
import com.codebreaker.dart.amazon.Product;
import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.database.Message;
import com.codebreaker.dart.display.ChatMessage;
import com.codebreaker.dart.zomato.Restaurant;
import com.codebreaker.dart.zomato.ZomatoHelper;
import com.codebreaker.dart.zomato.ZomatoListener;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chatbot extends Fragment implements AmazonListener, ZomatoListener {


    public Chatbot() {
        // Required empty public constructor
    }

    public Bot bot;
    public static Chat chat;

    private DatabaseHandler handler;
    private ArrayList<Message> messages = new ArrayList<>();

    //api helpers
    AmazonHelper amazonhelper;
    ZomatoHelper zomatoHelper;

    //UI components
    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatbot, container, false);

        //assigning helper classes
        amazonhelper = new AmazonHelper();
        zomatoHelper = new ZomatoHelper();

        //assigning interfaces
        amazonhelper.setOnAmazonListener(this);
        zomatoHelper.setOnZomatoListener(this);



        //setting ids for components
        mListView = (ListView) v.findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) v.findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) v.findViewById(R.id.et_message);
        mImageView = (ImageView) v.findViewById(R.id.iv_image);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mAdapter = new ChatMessageAdapter(getContext(), new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        loadEarlierMessages();

//code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();

                //extract keywords from message


                String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
                mimicOtherMessage(response);
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        initChatbot();
        return v;
    }

    private void initChatbot() {
        //checking SD card availablility
        boolean a = isSDCARDAvailable();
//receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
        boolean b = jayDir.mkdirs();
        Log.d("Here 3", "getting started " + b + " path " + Environment.getExternalStorageDirectory().toString());
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Hari")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Hari/" + dir)) {
                        Log.d("Here 2", "getting files");
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Hari/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                Log.d("Here", e.getMessage());
                e.printStackTrace();
            }
        }
//get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
//Assign the AIML files to bot for processing
        bot = new Bot("Hari", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);
    }

    private void loadEarlierMessages() {
        handler = new DatabaseHandler(getContext());
        messages = handler.getMessages();
        for(Message m : messages){
            // 1 is mine. 0 is bot.
            ChatMessage message = new ChatMessage(m.getMessage(), (m.getWho() == 1)?true:false, (m.getType().equalsIgnoreCase("text"))?false:true );
            mAdapter.add(message);
        }
        handler.close();

        scrollMyListViewToBottom();
    }

    private void saveMessage(String text, int who, String type){
        handler = new DatabaseHandler(getContext());
        Message msg = new Message();
        msg.setType(type);
        msg.setMessage(text);
        msg.setWho(who);
        handler.addMessage(msg);
        handler.close();
    }


    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }
    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    //Request and response of user and the bot
    public static void mainFunction (String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        System.out.println("Human: "+request);
        System.out.println("Robot: " + response);
    }

    //Sending Message Methods
    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        //chatMessage.setImagesource(getResources().getDrawable(R.drawable.bot, getTheme()));
        mAdapter.add(chatMessage);
        saveMessage(message, 1, "TEXT");
        //respond as Helloworld
        //mimicOtherMessage("HelloWorld");
    }

    private void mimicOtherImage(String url){
        ChatMessage message = new ChatMessage(url, false, true);
        mAdapter.add(message);
        saveMessage(url, 0, "IMAGE");
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage;

        if(message.length()<6){
            chatMessage = new ChatMessage(message, false, false);
            //chatMessage.setImagesource(getResources().getDrawable(R.drawable.bot, getTheme()));
            mAdapter.add(chatMessage);
            saveMessage(message, 0, "TEXT");
            return;
        }

        switch(message.substring(0,6)){

            case "AMZBUY":
                String item = message.substring(6, message.length());
                amazonhelper.getDataforSellingItems(item);
                chatMessage = new ChatMessage("Please wait..", false, false);
                mAdapter.add(chatMessage);
                break;

            case "ZMTLOC":
                zomatoHelper.makeLocationBasedRequest(getContext());
                chatMessage = new ChatMessage("Please wait..", false, false);
                mAdapter.add(chatMessage);
                break;

            default:
                chatMessage = new ChatMessage(message, false, false);
                //chatMessage.setImagesource(getResources().getDrawable(R.drawable.bot, getTheme()));
                mAdapter.add(chatMessage);
        }

        if(!message.equalsIgnoreCase("zmtloc") || !message.equalsIgnoreCase("amzbuy"))
            saveMessage(message, 0, "TEXT");

    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void getData(final List<Product> products) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Product p : products){
                    mimicOtherMessage(p.getName());
                    mimicOtherImage(p.getImageUrl());
                    mimicOtherMessage("Price : ₹ " + p.getPrice());
                }

                scrollMyListViewToBottom();
            }
        });

    }

    @Override
    public void getLocationBasedRestaurants(final List<Restaurant> restaurants) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Restaurant r : restaurants) {
                    mimicOtherMessage(r.getName() + '\n' + r.getAddress() + '\n' + r.getRating() + '\n' + r.getCusines());
                    mimicOtherImage(r.getImageUrl());
                }
                scrollMyListViewToBottom();
            }
        });
    }

}
