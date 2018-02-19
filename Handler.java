/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.util.Arrays;

/**
 *
 * @author alex
 */
public class Handler {
    //https://en.wikipedia.org/wiki/Paradigm
    //https://en.wikipedia.org/wiki/Cell_culture
    //https://en.wikipedia.org/wiki/Mimesis
    //https://en.wikipedia.org/wiki/Logic
    //https://en.wikipedia.org/wiki/Physics
    //https://en.wikipedia.org/wiki/Crystallography
    //https://en.wikipedia.org/wiki/Thermodynamics

    private Urlparser parser = new Urlparser();
    private Btree bt;
    Stree st;

    private static final String FDP = "C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\files";

    private ArrayList<HashTable> allsites = new ArrayList<>();
    private String url[] = new String[10];

    public Handler() throws Exception {
        url[0] = "https://en.wikipedia.org/wiki/Culture";
        url[1] = "https://en.wikipedia.org/wiki/Art";
        url[2] = "https://en.wikipedia.org/wiki/Mathematics";
        url[3] = "https://en.wikipedia.org/wiki/Science";
        url[4] = "https://en.wikipedia.org/wiki/Computer_science";
        url[5] = "https://en.wikipedia.org/wiki/History";
        url[6] = "https://en.wikipedia.org/wiki/Music";
        url[7] = "https://en.wikipedia.org/wiki/Biology";
        url[8] = "https://en.wikipedia.org/wiki/Language";
        url[9] = "https://en.wikipedia.org/wiki/Technology";
        st = new Stree();
    }

    void update() throws Exception {
        ArrayList<String> defs = new ArrayList<>();
        ArrayList<String> de = new ArrayList<>();

        if (fileEmpty()) {
            for (int y = 0; y < url.length; y++) {
                defs.add(url[y]);
                String d = parser.dateparse(url[y]);
                SD k = new SD(url[y], d);
                HashTable c = wordparser(k);
                c.sitedata.cluster = url[y];
                allsites.add(c);
                System.out.println(url[y] + " added to all");
            }
            for (int y = 0; y < url.length; y++) {
                ArrayList<String> l = parser.urlfinder(url[y]);
                for (int i = 0; i < 100; i++) {
                    if (allsites.size() < 1010) {
                        if (!defs.contains(l.get(i))) {
                            String d = parser.dateparse(l.get(i));
                            SD k = new SD(l.get(i), d);
                            k.parent = url[y];
                            HashTable c = wordparser(k);
                            allsites.add(c);
                            de.add(l.get(i));
                            l.add(l.get(i));
                            defs.add(l.get(y));
                            System.out.println(i + " added:" + l.get(i));
                        }
                    } else {
                        break;
                    }

                }
            }
            convertall(allsites);
            System.out.println("All sites were made");
            mBT();
            System.out.println("Btree full made");

        } else {
            File folder = new File(FDP);
            File[] listoffiles = folder.listFiles();
            for (int i = 0; i < listoffiles.length; i++) {
                HashTable ht = tableCaller(listoffiles[i]);
                if (ht != null) {
                    SD check = ht.sitedata;
                    String date = check.date;
                    String site = check.site;
                    if (parser.updatedate(site, date) == false) {
                        listoffiles[i].delete();
                        String newdate = parser.dateparse(site);
                        check.date = newdate;
                        HashTable newsite = wordparser(check);
                        filecreator(newsite);
                        System.out.println("updated");
                    }
                    System.out.println("good");
                } else {
                    System.out.println("ht was null-->find why");
                }
            }
        }
    }

    boolean streeEmpty() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\btree\\StreeData"));
        return br.readLine() == null;
    }

    void makeTree() throws Exception {
        for(int i=0;i<url.length;i++){
            SD k = new SD(url[i], null);
            HashTable c = wordparser(k);
            ArrayList<String> t=parser.urlfinder(url[i]);
            for(int j=0;j<100;j++){
                SD l = new SD(t.get(j),null);
                HashTable f = wordparser(l);
                Sim sim = new Sim();
                double o =sim.cosine(c, f);
                st.add(url[i], t.get(j), o);
                System.out.println(t.get(j)+" added");
            }
        }
        writetree(st);
        System.out.println("stree written");
    }
    ArrayList<String> getPath(String s) {
        ArrayList<String> key = new ArrayList<>();
        for(int i=0;i<url.length;i++){
            key.add(url[i]);
        }
        ArrayList<String> path = new ArrayList<>();                
        path =st.get(s, path, key);
        return path;
    }

   

    private void writetree(Stree xx) throws Exception {
        OutputStream op;
        ObjectOutputStream oos;
        op = new FileOutputStream("C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\btree\\StreeData", false);
        oos = new ObjectOutputStream(op);
        oos.writeObject(xx);
        oos.close();
        System.out.println("wrote tree");
    }

    void callTree() throws Exception {
        File file = new File("C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\btree\\StreeData");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Stree s = (Stree) ois.readObject();
        ois.close();
        st = s;
        System.out.println("st made");
    }


    String ssa(String x) throws Exception {
        SD a = new SD(x, null);
        HashTable c = wordparser(a);
        ArrayList<HashTable> l = callAll();
        Cluster cl = new Cluster(l);
        HashTable b = cl.clust(c);
        return b.sitedata.cluster;
    }

    private ArrayList<HashTable> callAll() throws Exception {
        ArrayList<HashTable> e = new ArrayList<>();
        File folder = new File(FDP);
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            HashTable m = tableCaller(files[i]);
            e.add(m);
            System.out.println("got table");
        }
        return e;
    }

    String ss(String x) throws Exception {
        ArrayList<HashTable> l = new ArrayList<>();
        SD a = new SD(x, null);
        HashTable c = wordparser(a);
        for (String url1 : url) {
            SD k = new SD(url1, null);
            HashTable ck = wordparser(k);
            l.add(ck);
        }
        Cluster cl = new Cluster(l);
        HashTable e = cl.clust(c);
        return e.sitedata.cluster;
    }

    private HashTable wordparser(SD da) throws Exception {
        HashTable s = new HashTable(da);
        ArrayList def = new ArrayList();
        ArrayList arraytemp = parser.URLparser(da.site);
        for (int j = 0; j < arraytemp.size(); j++) {
            String temp = arraytemp.get(j).toString();
            if (!def.contains(temp)) {
                int co = Collections.frequency(arraytemp, temp);
                Word word = new Word(temp, co);
                s.put(temp, word);
            }
            def.add(temp);
        }

        def.clear();
        return s;
    }

    void mBT() throws Exception {
        Btree btr = new Btree();
        File folder = new File(FDP);
        File[] files = folder.listFiles();
        int[] jk = new int[files.length];
        for (int i = 0; i < files.length; i++) {
            String p = files[i].getName();
            jk[i] = Integer.parseInt(p);
            System.out.println(p + " converted");
        }
        Arrays.sort(jk);
        System.out.println("Array sorted");
        for (int i = 0; i < jk.length; i++) {
            btr.insert(jk[i]);
            System.out.println(jk[i] + " added to btree");
        }
        bt = btr;
        btreefilecreator(bt);
        System.out.println("Btree file saved");
    }

    boolean fileEmpty() throws Exception {
        boolean k;
        File folder = new File(FDP);
        File[] listoffiles = folder.listFiles();
        k = listoffiles.length == 0;
        return k;
    }

    private void convertall(ArrayList<HashTable> e) throws Exception {
        for (int i = 0; i < e.size(); i++) {
            filecreator(e.get(i));
            System.out.println("file: " + i + " made");
        }
    }

    private void filecreator(HashTable x) throws Exception {
        OutputStream op;
        ObjectOutputStream oos;
        String m = x.sitedata.site;
        op = new FileOutputStream(FDP + "\\" + Math.abs(m.hashCode()), false);
        oos = new ObjectOutputStream(op);
        oos.writeObject(x);
        oos.close();
        System.out.println(m + " converted to file. " + m.hashCode());

    }

    private void btreefilecreator(Btree x) throws Exception {
        try {
            OutputStream op;
            ObjectOutputStream oos;
            op = new FileOutputStream("C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\btree\\btree", false);
            oos = new ObjectOutputStream(op);
            oos.writeObject(x);
            oos.close();
            System.out.println("Btree made");
        } catch (Exception e) {
            System.out.println("Btree not made cause: " + e);
        }
    }

    void btreefilereader() {
        File file = new File("C:\\Users\\shado\\Documents\\NetBeansProjects\\CSC365project1\\src\\btree\\btree");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Btree b = (Btree) ois.readObject();
            ois.close();
            bt = b;
            System.out.println("bt made");

        } catch (Exception e) {
            System.out.println("Btree didnt read " + e);
        }

    }

    private HashTable tableCaller(File x) throws Exception {
        File file = x;
        HashTable t;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashTable ht = (HashTable) ois.readObject();
            t = ht;
            ois.close();
        } catch (Exception e) {
            t = null;
        }

        return t;
    }

    boolean checkBtree(String x) throws Exception {
        Integer b = bt.search(bt.root, Math.abs(x.hashCode()));
        return b != null;
    }

    String dataCall(String x) throws Exception {
        File f = new File(FDP + "\\" + x.hashCode());
        HashTable v = tableCaller(f);
        String z = v.sitedata.cluster;
        return z;
    }
}
