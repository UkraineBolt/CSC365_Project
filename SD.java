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
public class SD implements java.io.Serializable{
    String site;
    String date;
    String cluster;
    String parent;
    public SD(String s,String d){
        site=s;
        date=d;
        cluster=null;
        parent=null;
    }
    
}
