/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

/**
 *
 * @author alex
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//DONE
public class Urlparser {
    //ArrayList<String> array = new ArrayList<>();

    ArrayList<String> URLparser(String x) throws Exception {
        ArrayList<String> array = new ArrayList<>();
        URL oracle = new URL(x);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine, sort, lowersort;
        String[] words;
        Element patt;
        Document doc;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            doc = Jsoup.parse(inputLine);
            patt = doc.select("p").first();
//gives all p attrs with encompasing links--need to seperate link from link word
            if (patt != null) {//gets rid of nulls
                sort = patt.text();//grabs text
                lowersort = sort.toLowerCase().trim();
                words = lowersort.split("\\W+");
                for (String word : words) {
                    array.add(word);
                }
            }

        }
        in.close();
        return array;
    }

    ArrayList<String> urlfinder(String x) throws Exception {
        ArrayList<String> array = new ArrayList<>();
        URL oracle = new URL(x);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine, site;
        Element patt;
        Document doc;
        while ((inputLine = in.readLine()) != null) {
            doc = Jsoup.parse(inputLine);
            patt = doc.select("a").first();//need to find time of last update
            if (patt != null) {
                site = patt.attr("href").trim();
                if (!site.contains("%") && (!site.contains("File")&&!site.contains("Main")&&!site.contains("Help")&&!site.contains(".jpg")&&!site.contains(".pdf")&&site.startsWith("/wiki/"))) {
                    site = "https://en.wikipedia.org" + site;
                    array.add(site);
                }
            }
        }
        in.close();
        return array;
    }

    Boolean updateDate(String u, String d) throws Exception {
        String date = dateparse(u);
        if (date.equals(d)) {
            return true;
        } else {
            return false;
        }
    }

    String dateparse(String x) throws Exception {
        String date = null;
        URL oracle = new URL(x);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        String inputLine;
        Element patt;
        Document doc;
        while ((inputLine = in.readLine()) != null) {
            doc = Jsoup.parse(inputLine);
            patt = doc.select("li").first();
            if (patt != null) {
                if (patt.text().contains("This page was last edited on")) {
                    String c = patt.text();
                    int d = c.indexOf("on") + 3;
                    date = c.substring(d);
                }
            }
        }
        in.close();
        return date;
    }

    
    
    Boolean updatedate(String u, String d) throws Exception {
        return true;
    }
}
