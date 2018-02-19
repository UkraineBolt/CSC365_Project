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
public class Word implements java.io.Serializable{
    String word;
    int count;
    public Word(String w,int c){
        word = w;
        count=c;
        
    }
    int getCount(){
        return count;
    }
    
    
}
