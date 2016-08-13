package com.ccthanking.framework.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Encipher{
    public static String EncodePasswd(String strPasswd){

        String strEncodePasswd = new String("");

        int i;

        int n;

        char code;

        String des = new String();

        String strKey = new String();

        if((strPasswd == null) || strPasswd.length() == 0){

            strEncodePasswd = "";

            return strEncodePasswd;

        }

        strKey = EncipherConst.m_strKey1 + EncipherConst.m_strKey2 + EncipherConst.m_strKey3 + EncipherConst.m_strKey4 + EncipherConst.m_strKey5 + EncipherConst.m_strKey6;

        while(strPasswd.length() < 8){

            strPasswd = strPasswd + (char)1;

        }

        des = "";

        for(n = 0; n <= strPasswd.length() - 1; n++){

            while(true){

                code = (char)Math.rint(Math.random() * 100);

                while((code > 0) && (((code ^ strPasswd.charAt(n)) < 0) || ((code ^ strPasswd.charAt(n)) > 90))){

                    code = (char)((int)code - 1);

                }

                char mid = 0;

                int flag;

                flag = code ^ strPasswd.charAt(n);

                if(flag > 93){

                    mid = 0;

                } else{

                    mid = strKey.charAt(flag); //Asc(Mid(strKey, (code Xor Asc(Mid(strPasswd, n, 1))) + 1, 1))

                }

                if((code > 35) & (code < 122) & (code != 124) & (code != 39) & (code != 44) & (mid != 124) & (mid != 39) & (mid != 44)){

                    break;

                }

                //确保生成的字符是可见字符并且在SQL语句中有效

            }

            char temp = 0;

            temp = strKey.charAt(code ^ strPasswd.charAt(n));

            des = des + (char)code + temp;

        }

        strEncodePasswd = des;

        return strEncodePasswd;

    }

    public static String DecodePasswd(String varCode){

        int n;

        String des = new String();

        String strKey = new String();

        if((varCode == null) || (varCode.length() == 0)){

            return "";

        }

        strKey = EncipherConst.m_strKey1 + EncipherConst.m_strKey2 + EncipherConst.m_strKey3 + EncipherConst.m_strKey4 + EncipherConst.m_strKey5 + EncipherConst.m_strKey6;

        if(varCode.length() % 2 == 1){

            varCode = varCode + "?";

        }

        des = "";

        for(n = 0; n <= varCode.length() / 2 - 1; n++){

            char b;

            b = varCode.charAt(n * 2);

            int a;

            a = (int)strKey.indexOf(varCode.charAt(n * 2 + 1));

            des = des + (char)((int)b ^ a);

        }

        n = des.indexOf(1);

        if(n > 0){

            return des.substring(0, n);

        } else{

            return des;

        }

    }

    static class EncipherConst{
        public final static String m_strKey1 = "zxcvbnm,./asdfg";

        public final static String m_strKeya = "cjk;";

        public final static String m_strKey2 = "hjkl;'qwertyuiop";

        public final static String m_strKeyb = "cai2";

        public final static String m_strKey3 = "[]\\1234567890-";

        public final static String m_strKeyc = "%^@#";

        public final static String m_strKey4 = "=` ZXCVBNM<>?:LKJ";

        public final static String m_strKeyd = "*(N";

        public final static String m_strKey5 = "HGFDSAQWERTYUI";

        public final static String m_strKeye = "%^HJ";

        public final static String m_strKey6 = "OP{}|+_)(*&^%$#@!~";

    }
    public static void  main(String[] args) throws Exception{


//      System.out.println("input:");
//      DataInputStream in = new DataInputStream(System.in);
//      String ss = in.readLine();
//      System.out.println(DecodePasswd(ss));
        
//        System.out.println(EncodePasswd("123456"));
//        System.out.println(DecodePasswd("<f=h?d?s?a?/R)8?"));
//        System.out.println(DecodePasswd("?g?f?d?s?a$l<JU("));
//        System.out.println(DecodePasswd("c)?m9/7c?g?.%3X$"));
//        
//        System.out.println(1^0);
//        
//        for (int i = 0; i < 10; i++) {
//			System.out.println(Math.rint(Math.random()*100));
//		}
    	System.out.println("请输入翻译前密码:");       
        BufferedReader reader = new BufferedReader(new InputStreamReader(       
                System.in));       
        String input = "";       
        input = reader.readLine();       
        String res = DecodePasswd(input);       
        System.out.println("密码:" + res);       
        reader.readLine();       
      }
      //System.out.println(DecodePasswd("={be*#WC\`dd%IP<T-"));


}