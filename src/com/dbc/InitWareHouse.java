package com.dbc;

import java.io.*;
import java.sql.*;
import java.util.*;

public class InitWareHouse {
    
    private String[] data;
    
    @SuppressWarnings("deprecation")
    public void initProduct()  {
        try {
            int n=7;char ch;
            Random rand = new Random();
            String temp;StringBuffer buf;
            PrintWriter writer;
            data=new String[n];
            
            writer=new PrintWriter("text/db/product.txt");
            rand.setSeed(46541237854L);
            for(int i=0;i<500;i++){
                buf=new StringBuffer("");
                for(int j=0;j<7;j++)
                    data[j]="";
                temp=String.valueOf(i);
                
                n=temp.length();
                data[0]="wh2008";
                for(int j=n;j<4;j++)
                    data[0]+="0";
                data[0]+=temp;
                
                for (int j = 0; j < 5; j++) {
                    n = Math.abs(rand.nextInt())%26+97;
                    ch = (char)n;
                    data[1]+=ch;
                    
                    n = Math.abs(rand.nextInt())%26+97;
                    ch = (char)n;
                    
                    data[4]+=ch;
                    n = Math.abs(rand.nextInt())%26+97;
                    ch = (char)n;
                    data[4]+=ch;
                }
                data[2]=""+Math.rint(Math.random()*100)/100;
                data[3]=""+Math.rint(Math.random()*100)/100;
                data[5]=""+Math.abs(rand.nextInt()%200);
                Timestamp randomDate=new Timestamp(new java.util.Date().getTime());
                data[6]=randomDate.toLocaleString();
                                //replaced by DateFormat.format(Date date).
                for(int j=0;j<6;j++){
                    buf.append(data[j]);
                    buf.append(",");
                }
                buf.append(data[6]);
                writer.println(buf);
            }
            writer.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
    }
    
    public void initEmployee(){
        int n=6;char ch;
        Random rand = new Random();
        String temp;
        StringBuffer buf;
        PrintWriter writer;
        data=new String[n];
        
        rand.setSeed(46541237854L);
        try {
            writer = new PrintWriter("text/db/employees.txt");
            for (int i = 0; i < 100; i++) {
                buf=new StringBuffer("");
                for(int j=0;j<6;j++)
                    data[j]="";
                temp=String.valueOf(i);
                
                n=temp.length();
                data[0]="emp2008";
                for(int j=n;j<3;j++)
                    data[0]+="0";
                data[0]+=temp;

                data[5]+="8866";
                for (int j = 0; j < 4; j++) {
                    n = Math.abs(rand.nextInt())%26+97;
                    ch = (char)n;
                    data[1]+=ch;
                    
                    n = Math.abs(rand.nextInt())%10+48;
                    ch = (char)n;
                    data[5]+=ch;
                }
                data[2]=""+(Math.abs(rand.nextInt()%50)+16);
                data[3]=(rand.nextInt()%2==0)?"男":"女";
                data[4]=(rand.nextInt()%20==0)?"主管":"普通职员";
                for(int j=0;j<5;j++){
                    buf.append(data[j]);
                    buf.append(",");
                }
                buf.append(data[5]);
                writer.println(buf);                
            }
            writer.close();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void initPassword(){
        int n=3;char ch;
        Random rand = new Random();
        String temp;
        StringBuffer buf;
        PrintWriter writer;
        data=new String[n];
        
        rand.setSeed(46541237854L);
        try {
            writer = new PrintWriter("text/db/empPass.txt");
            for (int i = 0; i < 100; i++) {
                buf=new StringBuffer("");
                for(int j=0;j<3;j++)
                    data[j]="";
                temp=String.valueOf(i);
                
                n=temp.length();
                data[0]="emp2008";
                for(int j=n;j<3;j++)
                    data[0]+="0";
                data[0]+=temp;

                for (int j = 0; j < 4; j++) {
                    n = Math.abs(rand.nextInt())%26+97;
                    ch = (char)n;
                    data[1]+=ch;
                }
                data[2]=""+Math.abs(rand.nextInt()%3);
                for(int j=0;j<2;j++){
                    buf.append(data[j]);
                    buf.append(",");
                }
                buf.append(data[2]);
                writer.println(buf);                
            }
            writer.close();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void initWareHouse(){
        int n=3;
        Random rand = new Random();
        String temp;
        StringBuffer buf;
        PrintWriter writer;
        data=new String[n];
        
        rand.setSeed(46541237854L);
        try {
            writer = new PrintWriter("text/db/wareHouse.txt");
            for (int i = 0; i < 5; i++) {
                buf=new StringBuffer("");
                for(int j=0;j<3;j++)
                    data[j]="";
                temp=String.valueOf(i);
                data[0]="wareHouse"+temp;
                data[2]+=(i%2==0)?"楼上":"地下室";
                data[1]=""+Math.abs(rand.nextInt()%3000);
                for(int j=0;j<2;j++){
                    buf.append(data[j]);
                    buf.append(",");
                }
                buf.append(data[2]);
                writer.println(buf);                
            }
            writer.close();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public static void main(String[] args) {
        InitWareHouse asdf=new InitWareHouse();
        asdf.initProduct();
        asdf.initEmployee();
        asdf.initPassword();
        asdf.initWareHouse();
    }
}
