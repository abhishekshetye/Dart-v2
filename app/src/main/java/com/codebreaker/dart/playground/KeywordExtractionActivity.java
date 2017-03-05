package com.codebreaker.dart.playground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codebreaker.dart.R;
import com.codebreaker.dart.amazon.AmazonListener;
import com.codebreaker.dart.amazon.Product;
import com.codebreaker.dart.database.DatabaseHandler;
import com.codebreaker.dart.database.Message;
import com.codebreaker.dart.nlp.Keyword;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class KeywordExtractionActivity extends AppCompatActivity implements AmazonListener{
    private final OkHttpClient client = new OkHttpClient();
    DatabaseHandler handler;
    List<Message> messages = new ArrayList<>();
    List<String> interests = new ArrayList<>();
    String NEWSFEED_API = "sBBqsGXiYgF0Db5OV5tAw7w2w2z2uV0kwr2rFM99s0IDMfrSG8OBGXeB2iH4XmiX";
    List<Keyword> keywords = new ArrayList<>();
    String mess = "Kendall has small legs. Kendall has small hands. The developers of java were good. They developed handing apps";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_extraction);
//        NetworkUrl("https://api.newsriver.io/v2/search?query=title%3A%22table%20tennis%22&sortBy=discoverDate&sortOrder=DESC&limit=15");





        /***** INTEREST DATABASE *****/

//        interests.add("android");
//
//        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
////        handler.addInterest("android");
////        handler.addInterest("music", 3);
////        handler.addInterest("developer");
//        //handler.updateDBValue(interests);
//        handler.checkAndUpdateInterest();
//        messages = handler.getAllData();
//        handler.close();
//
//        for(Message m : messages){
//            Log.d("SLIMF", m.getMessage());
//        }
//
//        Log.d("SLIMF", messages.size() + " ");


        /****** ZOMATO API ******/

//
//        ZomatoHelper req = new ZomatoHelper();
//        Toast.makeText(this, req.makeLocationBasedRequest(this) , Toast.LENGTH_SHORT).show();

        //sqlite database test
//        handler = new DatabaseHandler(getApplicationContext());
//        Message msg = new Message();
//        msg.setType("TEXT");
//        msg.setMessage("My first message");
//        msg.setWho(1);
//        handler.addMessage(msg);
//        messages = handler.getMessages();
//        for(Message m : messages){
//            Log.d("SLIMF", m.getMessage());
//        }
//        handler.close();

        /***** KEYPHRASE EXTRATION *******/
//        String theSentence = "android developers are good in town. They have really good job.";
//        try {
//            String excuded = ExudeData.getInstance().filterStoppingsKeepDuplicates(theSentence);
//            StringReader reader = new StringReader(excuded);
//            StandardTokenizer source = new StandardTokenizer(Version.LUCENE_36, reader);
//            TokenStream tokenStream = new StandardFilter(Version.LUCENE_36, source);
//            ShingleFilter sf = new ShingleFilter(tokenStream);
//            sf.setOutputUnigrams(false);
//
//            CharTermAttribute charTermAttribute = sf.addAttribute(CharTermAttribute.class);
//            sf.reset();
//
//            while (sf.incrementToken()) {
//                Log.d("SLIMF", charTermAttribute.toString());
//            }
//
//            sf.end();
//            sf.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidDataException e) {
//            e.printStackTrace();
//        }

        /***** KEA *****/
//        String input = "The android developer is a good job.";
//        try {
//            Reader reader = new StringReader(input);
//            NGramTokenizer gramTokenizer = new NGramTokenizer(reader, 1, 3);
//            CharTermAttribute charTermAttribute = gramTokenizer.addAttribute(CharTermAttribute.class);
//            gramTokenizer.reset();
//
//            while (gramTokenizer.incrementToken()) {
//                String token = charTermAttribute.toString();
//                //Do something
//                Log.d("SLIMF", token);
//            }
//            gramTokenizer.end();
//            gramTokenizer.close();
//        }catch(Exception e){
//
//        }


        /****** AMAZON API REQUEST ******/
//        AmazonHelper helper = new AmazonHelper();
//        helper.setOnAmazonListener(this);
//        helper.getDataforSellingItems("Shoes");
//        Log.d("HELPER", " ");


        /***** KEY WORD WITH FREQUENCY EXTRACTION *****/
//
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

            }

        });
    }

    @Override
    public void getData(List<Product> products) {
        Log.d("HELPER", " " + products.size() );
        for(Product product : products){
            Log.d("HELPER", product.getName() + " " +  product.getPrice() );
        }
    }
}
