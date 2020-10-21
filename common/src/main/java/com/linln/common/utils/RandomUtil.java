package com.linln.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by HSY on 2018/10/15.
 */
public class RandomUtil {
    public static String getRandom620(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
            result += randInt;
        }
        return result;
    }

    public static String getRandomOrderId(Integer num){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }
    public static String getOrderIdByTimeUserId(String userId) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result+userId;
    }


    public static String getTransferIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        String messy="";
        for(int i=0;i<100;i++){
            messy+=random.nextInt(10+i);
        }
        int d=random.nextInt(92);
        result=messy.substring(d,d+4);
        return newDate+result+d;
    }


    private static Long randomNumber;
    private static Long curIndex = Long.valueOf(0L);
    public synchronized static Long getId() throws Exception {
        Long index = null;
        // 从0到999 curIndex*100 curIndex 100-99900
        index = (curIndex = curIndex.longValue() + 1L).longValue() % 1000L;
        if (curIndex.longValue() >= 1000L) {
            curIndex = 0L;
        }
        // 随机数 1-10
        randomNumber = Long.valueOf(new Random().nextInt(100));
        return (new Date()).getTime() * 100000L + index.longValue() * 100L
                + randomNumber.longValue();
    }




    public  static  void  main(String [] a) throws Exception {

        String idAll="";
        int j=0;
       for(int i=0;i<100000;i++){
           j++;
           System.out.println(j);
           Long aa=getId();
           if(idAll.indexOf(aa+"")>-1){
               System.out.println(aa);
               break;
           }
           idAll+="["+aa+"],";
       }

       System.out.println(idAll);
       System.out.println("jjjj:"+j);

//        Random random=new Random();
//        String messy="";
//        for(int i=0;i<50;i++){
//            messy+=random.nextInt(10);
//        }
//        int d=random.nextInt(42);
//        System.out.println(messy.substring(d,d+4));

    }
    public static String getCaseNo(){
        Date date = new Date();
        String fix = "GA";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String str = fix+format.format(date);
        Random random = new Random();
        str += random.nextInt(100)+899;
        return str;
    }
}
