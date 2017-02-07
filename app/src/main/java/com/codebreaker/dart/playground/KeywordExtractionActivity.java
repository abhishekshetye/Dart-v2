package com.codebreaker.dart.playground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codebreaker.dart.AmazonProductAd;
import com.codebreaker.dart.R;
import com.codebreaker.dart.amazon.AmazonHelper;
import com.codebreaker.dart.amazon.AmazonListener;
import com.codebreaker.dart.amazon.Product;
import com.codebreaker.dart.nlp.Extract;
import com.codebreaker.dart.nlp.Keyword;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeywordExtractionActivity extends AppCompatActivity implements AmazonListener{

    List<Keyword> keywords = new ArrayList<>();
    String mess = "Kendall has small legs. Kendall has small hands. The developers of java were good. They developed handing apps";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_extraction);

        AmazonHelper helper = new AmazonHelper();
        helper.setOnAmazonListener(this);
        helper.getDataforSellingItems("Shoes");
        Log.d("HELPER", " ");
//        try {
//            String excuded = ExudeData.getInstance().filterStoppingsKeepDuplicates(mess);
//            Log.d("KEYWORD", excuded);
//            keywords = Extract.guessFromString(excuded);
//            Log.d("KEYWORD", keywords.size() + " ");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidDataException e) {
//            e.printStackTrace();
//        }
//
//        for(Keyword keyword: keywords){
//            for(String word: keyword.getTerms()){
//                Log.d("KEYWORD", word);
//            }
//            Log.d("KEYWORD", "previous freq is " + keyword.getFrequency());
//        }

    }

    @Override
    public void getData(List<Product> products) {
        Log.d("HELPER", " " + products.size() );
        for(Product product : products){
            Log.d("HELPER", product.getName() + " " +  product.getPrice() );
        }
    }
}
