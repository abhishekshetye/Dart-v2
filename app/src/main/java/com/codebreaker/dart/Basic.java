package com.codebreaker.dart;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.codebreaker.dart.adapters.ChatMessageAdapter;
import com.codebreaker.dart.amazon.AmazonHelper;
import com.codebreaker.dart.amazon.AmazonListener;
import com.codebreaker.dart.amazon.Product;
import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.database.Message;
import com.codebreaker.dart.display.ChatMessage;
import com.codebreaker.dart.zomato.GPSTracker;

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

public class Basic extends AppCompatActivity implements AmazonListener {

    private static final int PERMISSION_WRITE_REQUEST_CODE = 4;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 5;


    public Bot bot;
    public static Chat chat;

    private DatabaseHandler handler;
    private ArrayList<Message> messages = new ArrayList<>();

    //UI components
    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        if(!checkPermission()){
//            requestWritePermission();
//            requestLocationPermission();
//        }
//
//        if(!checkPermission())
//        {
//            Toast.makeText(this, "Please give all permissions", Toast.LENGTH_SHORT).show();
//            return;
//        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }







        //setting ids for components
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        loadEarlierMessages();

//code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
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
        handler = new DatabaseHandler(getApplicationContext());
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
        handler = new DatabaseHandler(getApplicationContext());
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

    public void event(View v){


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locresult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED && locresult == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void requestWritePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_REQUEST_CODE);
        }
    }

    private void requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;

            case PERMISSION_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;

        }
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
                AmazonHelper helper = new AmazonHelper(); //write this on top
                helper.setOnAmazonListener(this);
                helper.getDataforSellingItems(item);
                chatMessage = new ChatMessage("Please wait..", false, false);
                mAdapter.add(chatMessage);
                break;

            default:
                chatMessage = new ChatMessage(message, false, false);
                //chatMessage.setImagesource(getResources().getDrawable(R.drawable.bot, getTheme()));
                mAdapter.add(chatMessage);
        }

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
        runOnUiThread(new Runnable() {
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

}
