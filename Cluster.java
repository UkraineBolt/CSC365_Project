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
public class Cluster {    
    private ArrayList<HashTable> clusters;
    Cluster(ArrayList<HashTable> x){
        clusters=x;
    }
    HashTable clust(HashTable x) throws Exception{
        ArrayList<Double> cos = new ArrayList<>();
        for(int i=0;i<clusters.size();i++){
            Sim s = new Sim();
            double b =s.cosine(clusters.get(i), x);
            cos.add(b);
        }
        HashTable v=sort(cos);
        x.sitedata.cluster=v.sitedata.site;
        return x;
    }
    private HashTable sort(ArrayList<Double> d){
        double cos = 0;
        int sitenum = 0;
        double temp;
        for(int i=0;i<d.size()-1;i++){
            if(d.get(i)<d.get(i+1)){
                temp=d.get(i+1);
                if(temp>cos){
                    cos=temp;
                    sitenum=i+1;
                }
            }else if(d.get(i)>d.get(i+1)){
                temp=d.get(i);
                if(temp>cos){
                    cos=temp;
                    sitenum=i;
                }
            }else{
                temp = d.get(i);
                if (temp > cos) {
                    cos = temp;
                    sitenum = i;
                }
            }
        }
        return clusters.get(sitenum);
    }
    
}
