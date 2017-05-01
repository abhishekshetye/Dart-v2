package com.codebreaker.dart.nlp;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static String[] stopwords = {"hi", "hello", "dislike", "*", ",", ".", "a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};


    public Extract(Context context){
        initSet(context);
    }

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


    public Set<String> tags;

    public void initSet(Context context){
        tags = new HashSet<>();
        try {
            readFromAssets(context, "tags.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            tags.add(mLine);
            mLine = reader.readLine();
        }
        reader.close();
       // return sb.toString();
    }
    /*** MY ALGORITHM **/


    public List<String> removeStopping(String m){
        m = m.toLowerCase();
        List<String> wordsList = new ArrayList<>();
        String[] arr = m.split(" ");
        for(String s: arr){
            wordsList.add(s);
        }
        for (int j = 0; j < stopwords.length; j++) {
            if (wordsList.contains(stopwords[j])) {
                wordsList.remove(stopwords[j]);//remove it
            }
        }
        for(String s: wordsList){
            Log.d("WORDS", s);
        }
        return wordsList;
    }

    public List<String> extract(String message){
        Log.d("INTRST", "In here -> " + message );
        message= message.replaceAll("[-+.^:,]","");
        List<String> bigrams = new ArrayList<>();
        List<String> newtypes;

        try {

            newtypes = removeStopping(message);

            for(int i=0; i<newtypes.size(); i++){
                for(int j=0; j<newtypes.size(); j++){
                    if(i == j) continue;

                    Log.d("INTRST", newtypes.get(i) + newtypes.get(j));
                    if(tags.contains(newtypes.get(i) + newtypes.get(j))){
                        bigrams.add(newtypes.get(i) + " " + newtypes.get(j));
                    }
                }
            }

        } catch (Exception e) {
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
