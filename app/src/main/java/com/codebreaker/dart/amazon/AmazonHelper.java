package com.codebreaker.dart.amazon;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.codebreaker.dart.AmazonProductAd;

import org.w3c.dom.Document;
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

/**
 * Created by abhishek on 2/7/17.
 */

public class AmazonHelper {

    AmazonWebServiceAuthentication myAuthentication;
    String yourAccessKeyId = "AKIAJGRDZTPBKD2ATT7A";
    String yourSecretAccessKey = "4CSEVmjvmR7qWnxNeidHF/+QHkShE4MCvIccCTNQ";
    String yourAssociateTag = "abhishek96-21";

    AmazonListener listener;

    boolean err = true;


    public void setOnAmazonListener(AmazonListener listener){
        this.listener = listener;
    }

    public void getDataforSellingItems(String item){
        myAuthentication = AmazonWebServiceAuthentication.create(yourAssociateTag, yourAccessKeyId, yourSecretAccessKey);
        final String requestUrl = AmazonProductAdvertisingApiRequestBuilder.forItemSearch(item)
                .createRequestUrlFor(AmazonWebServiceLocation.IN, myAuthentication);
        Log.d("URL ", requestUrl);


        try {
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override public void onFailure(Call call, final IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    err = false;
                    String res = response.body().string();
                    parse(res);
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //https://drive.google.com/open?id=0B-RfMH1VQ3fsN19aTm44SjBYa3c
    private final OkHttpClient client = new OkHttpClient();



    private void retriveImages(String asin){

        if(myAuthentication!=null)
            myAuthentication = AmazonWebServiceAuthentication.create(yourAssociateTag, yourAccessKeyId, yourSecretAccessKey);
        final String requestUrl = AmazonProductAdvertisingApiRequestBuilder.forItemLookup(asin, ItemId.Type.ASIN)
                .createRequestUrlFor(AmazonWebServiceLocation.IN, myAuthentication);
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
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                err = false;
                final String res = response.body().string();
                parseImages(res);
            }
        });

    }

    private void parse(String XMLs){

        List<Product> products = new ArrayList<>();

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

                    String tempPrice = ""; String title="";
                    for(int k=0; k<item.getChildNodes().getLength(); k++){
                        Node node = item.getChildNodes().item(k);
                        if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase("ASIN")){
                            Log.d("NEWNODE", "asin " + node.getTextContent());
                        }
                        else if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase("parentasin")){
                            Log.d("NEWNODE", "asin " + node.getTextContent());
                        }
                        else if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase("itemattributes")){
                            Log.d("NEWNODE", "asin " + node.getChildNodes().getLength() + " ");
                            for(int j=0; j<node.getChildNodes().getLength(); j++){
                                Node price = node.getChildNodes().item(j);
                                if(price.getNodeType() == Node.ELEMENT_NODE && price.getNodeName().equalsIgnoreCase("Listprice")){
                                    Log.d("PARSE", "price is " + price.getFirstChild().getTextContent());
                                    tempPrice = price.getFirstChild().getTextContent();
                                }else if(price.getNodeType() == Node.ELEMENT_NODE && price.getNodeName().equalsIgnoreCase("title")){
                                    title = price.getTextContent();
                                }
                            }
                        }
                    }

                    Product product = new Product();
                    product.setName(title);
                    product.setPrice(tempPrice);
                    products.add(product);
//                    Node asin = item.getFirstChild();
//                    Node detailpage = asin.getNextSibling();
//                    Node attr = detailpage.getNextSibling().getNextSibling();
//                    NodeList attrlist = attr.getChildNodes();
//
//                    for(int j=0; j<attrlist.getLength(); j++){
//                        Node price = attrlist.item(j);
//                        if(price.getNodeType() == Node.ELEMENT_NODE && price.getNodeName().equalsIgnoreCase("Listprice")){
//                            Log.d("PARSE", "price is " + price.getFirstChild().getTextContent());
//                            tempPrice = price.getFirstChild().getTextContent();
//                        }
//                    }
//                    Log.d("PARSE", "" + asin.getTextContent() + " " + detailpage.getTextContent());


                    //retriveImages(asin.getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listener.getData(products);

    }

}
