package com.linln.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SHA256Util {
    public static void main(String[] args) {
        String api_key="J4X29U6C";
        String api_seccet="E1O20HO0TZ2N3GAE";
        String time=(((new Date()).getTime())/1000)+"";


//        String sum=getSHA256StrJava(api_key+api_seccet+time);
//
//        String aa="https://bidong.lianxueqiu.com/api2/market/rank_list? type=1&api_key="+api_key+"&time="+time+"&sign="+sum;
//       System.out.println(aa);


       String appkey="Xz48uZsBDRAMrGsscBZqg3";
       String  masterSecret="2yLF4wrWxT9fW95GtrtAm2";
       String  dsdsdf=getSHA256StrJava(appkey+time+masterSecret);
        System.out.println(time);
       System.out.println(dsdsdf);
       time=((new Date()).getTime())+"";
        dsdsdf=getSHA256StrJava(appkey+time+masterSecret);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(time);
        System.out.println(dsdsdf);

//        System.out.println(getSHA256StrJava("130"));
//        System.out.println(getSHA256StrJava("130"));
    }



    public static String getSHA256StrJava(String str) {

        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
//1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }





}
