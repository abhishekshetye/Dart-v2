package com.codebreaker.dart;

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
import com.codebreaker.dart.amazon.ItemId;
import com.codebreaker.dart.amazon.ItemInformation;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AmazonProductAd extends AppCompatActivity {

    AmazonWebServiceAuthentication myAuthentication;
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

       getDataforSellingItems("Moto mobile");
    }


    public void getDataforSellingItems(String item){
        myAuthentication = AmazonWebServiceAuthentication.create(yourAssociateTag, yourAccessKeyId, yourSecretAccessKey);
        final String requestUrl = AmazonProductAdvertisingApiRequestBuilder.forItemSearch(item)
                .createRequestUrlFor(AmazonWebServiceLocation.IN, myAuthentication, 1);
        Log.d("URL ", requestUrl);

        try {
            runNetwork(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
            Err();
        }
    }


    void Err(){
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    void Succ(){
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
    //https://drive.google.com/open?id=0B-RfMH1VQ3fsN19aTm44SjBYa3c
    private final OkHttpClient client = new OkHttpClient();

    public void runNetwork(String url) throws Exception {
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

            @Override public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                err = false;
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(res);
                            parse(res);
                        } catch (Exception e) {
                            Log.d("ERROR", "here " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }


        });

    }

    private void retriveImages(String asin){

        if(myAuthentication!=null)
            myAuthentication = AmazonWebServiceAuthentication.create(yourAssociateTag, yourAccessKeyId, yourSecretAccessKey);
        final String requestUrl = AmazonProductAdvertisingApiRequestBuilder.forItemLookup(asin, ItemId.Type.ASIN)
                .createRequestUrlFor(AmazonWebServiceLocation.IN, myAuthentication, 0);
        Log.d("IMAGEURL", requestUrl);

        try {
            runImageRequest(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseImages(String data){
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(data)));
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Item");

            Log.d("PARSE", nList.getLength() + " ");

            for(int i=0; i<nList.getLength(); i++){
                Node item = nList.item(i);
                if(item.getNodeType() == Node.ELEMENT_NODE){

                    NodeList childofitem = item.getChildNodes();
                    String smallimg = "", mediumimg = "";

                    for(int j=0; j<childofitem.getLength(); j++){
                        if(childofitem.item(j).getNodeType() == Node.ELEMENT_NODE && childofitem.item(j).getNodeName().equalsIgnoreCase("smallimage")){
                            smallimg = childofitem.item(j).getFirstChild().getTextContent();
                        }
                        if(childofitem.item(j).getNodeType() == Node.ELEMENT_NODE && childofitem.item(j).getNodeName().equalsIgnoreCase("mediumimage")){
                            mediumimg = childofitem.item(j).getFirstChild().getTextContent();
                            break;
                        }
                    }
                    Log.d("PARSEIMG", "images are " + smallimg+ " " + mediumimg );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void runImageRequest(String url) throws Exception {
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

            @Override public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                err = false;
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //tv.setText(res);
                            parseImages(res);
                        } catch (Exception e) {
                            Log.d("ERROR", "here " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }


        });

    }

    private void parse(String XMLs){
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(XMLs)));
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Item");

            Log.d("PARSE", nList.getLength() + " ");

            for(int i=0; i<3 /*nList.getLength()*/; i++){
                Node item = nList.item(i);
                if(item.getNodeType() == Node.ELEMENT_NODE){
                    //Log.d("PARSE", item.getNodeName() + " ");
                    Node asin = item.getFirstChild();
                    retriveImages(asin.getTextContent());
                    Node detailpage = asin.getNextSibling();
                    Node attr = detailpage.getNextSibling().getNextSibling();
                    NodeList attrlist = attr.getChildNodes();

                    for(int j=0; j<attrlist.getLength(); j++){
                        Node price = attrlist.item(j);
                        if(price.getNodeType() == Node.ELEMENT_NODE && price.getNodeName().equalsIgnoreCase("Listprice")){
                            Log.d("PARSE", "price is " + price.getFirstChild().getTextContent());
                        }
                    }
                    Log.d("PARSE", "" + asin.getTextContent() + " " + detailpage.getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
