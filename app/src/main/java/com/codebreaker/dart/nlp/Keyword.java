package com.codebreaker.dart.nlp;



import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by abhishek on 1/22/17.
 */

public class Keyword implements Comparable<Keyword> {

    private final String stem;
    private final Set<String> terms = new HashSet<String>();
    private int frequency = 0;

    public Keyword(String stem) {
        this.stem = stem;
    }

    public void add(String term) {
        terms.add(term);
        frequency++;
    }

    @Override
    public int compareTo(Keyword o) {
        // descending order
        return Integer.valueOf(o.frequency).compareTo(frequency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Keyword)) {
            return false;
        } else {
            return stem.equals(((Keyword) obj).stem);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { stem });
    }

    public String getStem() {
        return stem;
    }

    public Set<String> getTerms() {
        return terms;
    }

    public int getFrequency() {
        return frequency;
    }



}