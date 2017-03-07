package com.codebreaker.dart.nlp;

import android.util.Log;

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by abhishek on 1/22/17.
 */

public class Extract {

    // smile nlp extractor 

    public static String stem(String term) throws IOException {

        TokenStream tokenStream = null;
        try {

            // tokenize
            tokenStream = new ClassicTokenizer(Version.LUCENE_36, new StringReader(term));
            // stem
            tokenStream = new PorterStemFilter(tokenStream);

            // add each token in a set, so that duplicates are removed
            Set<String> stems = new HashSet<String>();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                stems.add(token.toString());
            }

            // if no stem or 2+ stems have been found, return null
            if (stems.size() != 1) {
                return null;
            }
            String stem = stems.iterator().next();
            // if the stem has non-alphanumerical chars, return null
            if (!stem.matches("[a-zA-Z0-9-]+")) {
                return null;
            }

            return stem;

        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }

    }

    public static <T> T find(Collection<T> collection, T example) {
        for (T element : collection) {
            if (element.equals(example)) {
                return element;
            }
        }
        collection.add(example);
        return example;
    }


    public static List<Keyword> guessFromString(String input) throws IOException {

        List<Keyword> keywords = new LinkedList<Keyword>();
        TokenStream tokenStream = null;
        try {

            input = ExudeData.getInstance().filterStoppings(input);
            // hack to keep dashed words (e.g. "non-specific" rather than "non" and "specific")
            input = input.replaceAll("-+", "-0");
            // replace any punctuation char but apostrophes and dashes by a space
            input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
            // replace most common english contractions
            input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

            // tokenize input
            tokenStream = new ClassicTokenizer(Version.LUCENE_36, new StringReader(input));
            // to lowercase
            tokenStream = new LowerCaseFilter(Version.LUCENE_36, tokenStream);
            // remove dots from acronyms (and "'s" but already done manually above)
            tokenStream = new ClassicFilter(tokenStream);
            // convert any char to ASCII
            tokenStream = new ASCIIFoldingFilter(tokenStream);
            // remove english stop words
            tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, EnglishAnalyzer.getDefaultStopSet());

            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = token.toString();
                // stem each term
                String stem = stem(term);
                if (stem != null) {
                    // create the keyword or get the existing one if any
                    Keyword keyword = find(keywords, new Keyword(stem.replaceAll("-0", "-")));
                    // add its corresponding initial token
                    keyword.add(term.replaceAll("-0", "-"));
                }
            }

            // reverse sort by frequency
            Collections.sort(keywords);

            return keywords;

        } catch (InvalidDataException e) {
            e.printStackTrace();
        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }
        return keywords;
    }


    /*** MY ALGORITHM **/

    public List<String> extract(String message){

        List<String> bigrams = new ArrayList<>();
        try {
            String excluded = ExudeData.getInstance().filterStoppings(message).toLowerCase();
            String[] types = excluded.split(" ");
            Set<String> containing = new HashSet<>();
            containing.add("androidprogramming");
            containing.add("iosprogramming");
            for(int i=0; i<types.length; i++){
                for(int j=0; j<types.length; j++){
                    if(i == j) continue;

                    Log.d("INTRST", types[i] + types[j]);
                    if(containing.contains(types[i] + types[j])){
                        bigrams.add(types[i] + " " +  types[j]);
                    }
                }
            }

        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        return bigrams;
    }


    public List<String> extractKeyPhrase (String input){
        List<String> words = new ArrayList<>();
        try {
            String excuded = ExudeData.getInstance().filterStoppingsKeepDuplicates(input);
            StringReader reader = new StringReader(excuded);
            StandardTokenizer source = new StandardTokenizer(Version.LUCENE_36, reader);
            TokenStream tokenStream = new StandardFilter(Version.LUCENE_36, source);
            ShingleFilter sf = new ShingleFilter(tokenStream);
            sf.setOutputUnigrams(false);

            CharTermAttribute charTermAttribute = sf.addAttribute(CharTermAttribute.class);
            sf.reset();

            while (sf.incrementToken()) {
                Log.d("SLIMF", charTermAttribute.toString());
                words.add(charTermAttribute.toString());
            }

            sf.end();
            sf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        return words;
    }
}
