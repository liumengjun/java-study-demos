package com.userid;

public class IDSearchTest {
    
    public static void main(String[] args) {
        test02();
    }
    
    //搜索验证码
    public static void test02() {
        String id_str = "370101198807231234";
        byte[] id = id_str.getBytes();
        //System.out.println(id.length);
        for(int x=0;x<10;x++){
            byte x_byte = (byte)('0'+x);
            //System.out.println(x_byte);
            id[17] = x_byte;
            byte[] ok = CertificateOfIdentification.verifyID(new String(id));
            if(ok!=null){
                System.out.println(new String(ok));
            }else{
                //System.out.println("error:"+new String(id));
            }
        }
    }
    
    //搜索生日 XX月XX日
    public static void test01() {
        byte[] id ={'3','7','1','5','2','2','1','9','8','9','0','1','0','1','0','8','3','0'};
        for(int mon=1;mon<=12;mon++){
            String mstr=String.valueOf(mon);
            byte[] monbyte=mstr.getBytes();
            if(monbyte.length==1){
                id[10]='0';id[11]=monbyte[0];
            }else{
                id[10]=monbyte[0];id[11]=monbyte[1];
            }
            for(int day=1;day<=30;day++){
                String dstr=String.valueOf(day);
                byte[] daybyte=dstr.getBytes();
                if(daybyte.length==1){
                    id[12]='0';id[13]=daybyte[0];
                }else{
                    id[12]=daybyte[0];id[13]=daybyte[1];
                }
                
                byte[] ok = CertificateOfIdentification.verifyID(new String(id));
                if(ok!=null){
                    System.out.println(new String(ok));
                }
            }
        }
    }
}
