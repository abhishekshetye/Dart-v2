package com.codebreaker.dart.playground;

import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codebreaker.dart.R;
import com.codebreaker.dart.amazon.Item;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.List;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParsing extends AppCompatActivity {
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;

    String temp = "<?xml version=\"1.0\"?>\n" +
            "<class>\n" +
            "   <student rollno=\"393\">\n" +
            "      <firstname>dinkar</firstname>\n" +
            "      <lastname>kad</lastname>\n" +
            "      <nickname>dinkar</nickname>\n" +
            "      <marks>85</marks>\n" +
            "   </student>\n" +
            "   <student rollno=\"493\">\n" +
            "      <firstname>Vaneet</firstname>\n" +
            "      <lastname>Gupta</lastname>\n" +
            "      <nickname>vinni</nickname>\n" +
            "      <marks>95</marks>\n" +
            "   </student>\n" +
            "   <student rollno=\"593\">\n" +
            "      <firstname>jasvir</firstname>\n" +
            "      <lastname>singn</lastname>\n" +
            "      <nickname>jazz</nickname>\n" +
            "      <marks>90</marks>\n" +
            "   </student>\n" +
            "</class>";

    private static final String XMLs =
            "<novoda>" +
                    "<team>" +
                        "<name>Harvey</name>" +
                        "<name>Ben</name>" +
                        "<name>Carl</name>" +
                        "<name>David</name>" +
                        "<name>Franky</name>" +
                        "<name>Kevin</name>" +
                        "<name>Moe</name>" +
                        "<name>Paul</name>" +
                        "<name>Peter</name>" +
                        "<name>Shiv</name>" +
                    "</team>" +
                    "</novoda>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlparsing);

        int count = 0;

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(XMLs)));
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("name");

            for(int i=0; i<nList.getLength(); i++){
                Node name = nList.item(i);
                if (name.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) name;
                    System.out.println("Student name : "
                            + eElement.getTextContent());
                }

            }

//
//            System.out.print("New node " + newnode.getNodeName());
//            NodeList newlist = newnode.getChildNodes();
//            System.out.println("----------------------------");
//            for (int temp = 0; temp < nList.getLength(); temp++) {
//                Node nNode = nList.item(temp);
//                System.out.println("\nCurrent Element :"
//                        + nNode.getNodeName());
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element eElement = (Element) nNode;
//                    count++;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("COUNT", count + "");
    }

}
