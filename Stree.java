/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

import java.util.*;

/**
 *
 * @author alex
 */
public class Stree implements java.io.Serializable {

    class Node implements java.io.Serializable{

        String site;
        int treenum;
        boolean visted;
        ArrayList<Double> dweight;
        ArrayList<Node> dst;

        Node(String s) {
            site = s;
            dweight = new ArrayList<>();
            dst = new ArrayList<>();
            visted = false;
        }

        void changetreenumto(int x) {
            treenum = x;
            for (int i = 0; i < dst.size(); i++) {
                if (dst.get(i).treenum != treenum) {
                    dst.get(i).changetreenumto(x);
                }
            }
        }
    }
    ArrayList<Node> all;
    ArrayList<Integer> missing;
    int AOST;

    Stree() {
        all = new ArrayList<>();
        missing = new ArrayList<>();
    }

    private Node find(String x) {
        if (x != null) {
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).site.equals(x)) {
                    return all.get(i);
                }
            }
        }
        return null;
    }

    void add(String s, String d, double w) {
        if (!s.equals(d)) {
            Node scr = find(s);
            Node dest = find(d);
            if (scr == null && dest == null) {
                Node es = new Node(s);
                Node ed = new Node(d);
                AOST++;
                System.out.println("new tree made "+AOST);
                if (missing.isEmpty()) {
                    es.treenum = AOST;
                    ed.treenum = AOST;
                } else {
                    es.treenum = missing.get(0);
                    ed.treenum = missing.get(0);
                    missing.remove(0);
                    Collections.sort(missing);
                }
                es.dst.add(ed);
                es.dweight.add(w);
                ed.dst.add(es);
                ed.dweight.add(w);
                all.add(ed);
                all.add(es);
            } else if (scr != null && dest == null) {
                Node ed = new Node(d);
                scr.dst.add(ed);
                scr.dweight.add(w);
                ed.treenum = scr.treenum;
                ed.dst.add(scr);
                ed.dweight.add(w);
                all.add(ed);
            } else if (scr == null && dest != null) {
                Node es = new Node(s);
                es.dst.add(dest);
                es.dweight.add(w);
                es.treenum = dest.treenum;
                dest.dst.add(es);
                dest.dweight.add(w);
                all.add(es);
            } else if (scr != null && dest != null && missinglink(scr, dest)) {
                scr.dst.add(dest);
                scr.dweight.add(w);
                dest.dst.add(scr);
                dest.dweight.add(w);
                if (scr.treenum != dest.treenum) {//NOT FUN
                    AOST--;
                    System.out.println("Tree merged "+AOST);
                    if (scr.treenum > dest.treenum) {
                        missing.add(scr.treenum);
                        Collections.sort(missing);
                        scr.changetreenumto(dest.treenum);
                    } else {
                        missing.add(dest.treenum);
                        Collections.sort(missing);
                        dest.changetreenumto(scr.treenum);
                    }
                }
            } else {
                System.out.println("link is already established");
            }
        } else {
            System.out.println("scr and dest need to be different");
        }
    }

    private boolean missinglink(Node scr, Node dest) {
        for (int i = 0; i < scr.dst.size(); i++) {
            if (scr.dst.get(i).site.equals(dest.site)) {
                return false;
            }
        }
        return true;
    }

    ArrayList<String> get(String s, ArrayList<String> path, ArrayList<String> key) {
        resetsearch();
        Node b = find(s);        
        ArrayList<Node> k = new ArrayList<>();
        for (int i = 0; i < key.size(); i++) {
            Node l = find(key.get(i));
            if (l != null) {
                k.add(l);
            }
        }
        if (b != null && !k.isEmpty()) {
            return search(b, path, k);
        } else {
            return null;
        }
    }

    private ArrayList<String> search(Node site, ArrayList<String> path, ArrayList<Node> key) {
        site.visted = true;
        if (checkbase(site.site, key)) {
            String concat = "Cluster found:" + site.site;
            path.add(concat);
            return path;
        } else {
            path.add(site.site);
            for (int i = 0; i < site.dst.size(); i++) {
                Node xx = wchecknext(site, key);
                ArrayList<String> temp;
                if (xx == null) {
                    path.remove(path.size() - 1);
                    break;
                } else {
                    temp = search(xx, path, key);
                }
                if (temp != null && temp.get(temp.size() - 1).contains("Cluster")) {
                    return temp;
                }
            }
            return path;

        }

    }

    private Node wchecknext(Node x, ArrayList<Node> key) {
        double in = Integer.MIN_VALUE;
        int dex = -1;
        for (int i = 0; i < x.dst.size(); i++) {
            if (x.dst.get(i).visted == false) {
                if (key.contains(x.dst.get(i))) {
                    dex = i;
                    break;
                } else if (x.dweight.get(i) > in) {
                    dex = i;
                }
            }
        }
        if (dex > -1) {
            return x.dst.get(dex);
        } else {
            return null;
        }
    }

    private boolean checkbase(String x, ArrayList<Node> key) {
        for (int i = 0; i < key.size(); i++) {
            if (x.equals(key.get(i).site)) {
                return true;
            }
        }
        return false;
    }

    private void resetsearch() {
        for (int i = 0; i < all.size(); i++) {
            all.get(i).visted = false;
        }
    }

}
