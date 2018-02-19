/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

import java.util.ArrayList;
/**
 *
 * @author alex
 */
public class Sim {

    private Urlparser parser = new Urlparser();
    
    private HashTable searchsite;
    private double searchmagit;
    private HashTable basesite;
    private double basemagit;

    double cosine(HashTable s, HashTable b) throws Exception {
        searchsite=s;
        basesite=b;
        double ab = top();
        searchmagit = magmake(searchsite);
        basemagit = magmake(basesite);
        double value = ab / (basemagit * searchmagit);
        System.out.println("Cosine done");
        return value;
    }

    private double magmake(HashTable v) {
        ArrayList<Integer> fre = new ArrayList<>();
        ArrayList<Word> words = v.getAll();
        for (int k = 0; k < words.size(); k++) {
            fre.add(words.get(k).getCount());
        }
        double b = magitude(fre);
        return b;
    }

    private double magitude(ArrayList<Integer> a) {
        double count = 0;
        double finalcount;
        for (int i = 0; i < a.size(); i++) {
            double k = a.get(i);
            count += Math.pow(k, 2);
        }

        finalcount = Math.sqrt(count);
        return finalcount;

    }

    private double top() {//computates the A*B
        ArrayList<String> setA = new ArrayList();
        ArrayList<String> setB = new ArrayList();
        ArrayList<String> union = new ArrayList();
        ArrayList<Word> search;
        ArrayList<Word> base;
        HashTable a = searchsite;
        HashTable b = basesite;
        double sum = 0;
        search = a.getAll();
        base = b.getAll();
        for (int i = 0; i < search.size(); i++) {
            String bet = search.get(i).word;
            union.add(bet);
            setA.add(bet);
        }
        for (int i = 0; i < base.size(); i++) {
            String bet = base.get(i).word;
            union.add(bet);
            setB.add(bet);
        }
        union.retainAll(setA);
        union.retainAll(setB);//gets all words in a that are in b
        if (!union.isEmpty()) {
            for (int i = 0; i < union.size(); i++) {
                Word temp = a.get(union.get(i));
                Word tem = b.get(union.get(i));
                sum += ((temp.getCount()) * (tem.getCount()));
            }//mult count of word in both a and b together and adding to sum
        } else {

        }
        return sum;
    }

}
