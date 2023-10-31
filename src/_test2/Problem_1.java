package _test2;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Problem_1 {
    
    public static void main(String[] args) throws IOException {
        Properties property = System.getProperties();
        String str = property.getProperty("user.dir");
        System.out.println("当前工作路径:"+str);
        System.out.println(System.getProperty("user.dir","."));
        File file = new File(".");
        System.out.println(file.getPath()+"|"+file.getAbsolutePath()+"|"+file.getCanonicalPath());
        
        System.out.println("class文件路径"+System.getProperty("java.class.path"));
        Class<Problem_1> theClass = Problem_1.class;
        java.net.URL u = theClass.getResource("");
        System.out.println("The Java RunTime used is ocated at : " + u);
        
        int[] a={13,24,35};
        System.out.println("原数组");
        printA(a);
        //添加一个数
        int[] b = insert(a,56);
        System.out.println("添加一个新数值");
        printA(b);
        
        String str1 = "this is a sentence";
        String str2 = reverseSentence(str1);
        System.out.println(str1);
        System.out.println(str2);
        
        String[] s = str2.split(" ");
        for(int i=0;i<s.length;i++){
            System.out.println((i+1)+":"+s[i]);
        }
    }
    
    public static String reverseSentence(String a){
        String b = "";
        String temp=a;
        int p2 = temp.lastIndexOf(" ");
        while(p2!=-1){
            b += temp.substring(p2+1)+" ";
            temp = temp.substring(0, p2);
            p2 = temp.lastIndexOf(" ");
        }
        b += temp;
        return b;
    }
    
    public static int[] insert(int[] a, int newdata){
        int[] b = new int[a.length+1];
        int i;
        for(i=0;i<a.length;i++){
            if(a[i]<newdata)
                b[i]=a[i];
            else
                break;
        }
        b[i]=newdata;
        for(;i<a.length;i++)
            b[i+1]=a[i];
        return b;
    }
    
    public static void printA(int[] a){
        for(int i=0;i<a.length;i++)
            System.out.println(a[i]);
    }
}
