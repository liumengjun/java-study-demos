package utils.regex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexText {
    static void test(){
        Pattern p = null; //正则表达式      
        Matcher m = null; //操作的字符串  
        boolean b = false;
        /*
        //正则表达式表示首字母是a，中间是任意字符，结尾以b结束
        //不匹配的结果
        p = Pattern.compile("a*b");    
        m = p.matcher("baaaaab");    
        b = m.matches();    
        System.out.println("匹配结果："+b); //输出：false 
          
        //匹配的结果
        p = Pattern.compile("a*b");    
        m = p.matcher("aaaaab");    
        b = m.matches();    
        System.out.println("匹配结果："+b); //输出：true 
        */
        
        p = Pattern.compile("a[a-z]*b");    //正则表达式    
        m = p.matcher("aabaavabb");         //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);
    }
    
    public static void main(String argus[]){
        test();
        
        String reference = "h*";
        String current = "hello";
        
        System.out.println(reference);
        System.out.println(current);
        Pattern p = null; //正则表达式      
        Matcher m = null; //操作的字符串  
        boolean b = false;
        p = Pattern.compile("h(.)*");   //正则表达式    
        m = p.matcher("hel\tlo");           //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);
        
        p = Pattern.compile("h(.)*");   //正则表达式    
        m = p.matcher("hel\n\tlo");         //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);
        
        p = Pattern.compile(".");   //正则表达式    
        m = p.matcher("we");            //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);
        
        p = Pattern.compile(".");   //正则表达式    
        m = p.matcher("w");         //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);

        p = Pattern.compile("\\."); //正则表达式    
        m = p.matcher(".");         //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);

        p = Pattern.compile("\\."); //正则表达式    
        m = p.matcher("w");         //操作的字符串
        b = m.matches();    
        System.out.println("匹配结果："+b);
    }
}
