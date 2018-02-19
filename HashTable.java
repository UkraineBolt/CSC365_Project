/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class HashTable implements java.io.Serializable{
    
    int count;
    SD sitedata;
    LinkedList<Word> site[];

    public HashTable(SD d) {
        this.count = 0;
        this.site = new LinkedList[32];
        sitedata=d;
    }

    void put(String k, Word w) {
        //resize();//might need to commit out again
        int hash = k.hashCode();
        int ahash = Math.abs(hash);
        int tableI = ahash & (site.length - 1);
        if (site[tableI] != null) {
            site[tableI].add(w);
            count++;
            
        } else {
            site[tableI] = new LinkedList<>();
            site[tableI].add(w);
            count++;

        }
    }

    Word get(String a) {
        Word ret=null;
        int hash = a.hashCode();
        int ahash = Math.abs(hash);
        int tableI = ahash & (site.length - 1);
            for (int i = 0; i < site[tableI].size(); i++) {
                if (site[tableI].get(i).word.equals(a)) {
                    ret = site[tableI].get(i);
                    break;
                }
    }
        return ret;
    }

    void resize() {
        int key = (int) (site.length * .75);
        if (count == key) {
            LinkedList<Word>[] newsite = Arrays.copyOf(site, site.length*2);
            site = newsite;
        }
    }

    ArrayList<Word> getAll() {
        ArrayList<Word> beta = new ArrayList();
        for (int i = 0; i < site.length; i++) {
            if (site[i] != null) {
                for (int k = 0; k < site[i].size(); k++) {
                    if (site[i].get(k) != null) {
                        beta.add(site[i].get(k));
                    }
                }
            }
        }
        return beta;
    }

}
