package com.linln.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GetAccount {

    public static String  getaccount(){   //获取用户id
        Random random=new Random();
        int rannum= (int)(random.nextDouble()*(10000000));
        for (;;){
            rannum= (int)(random.nextDouble()*(10000000));
            if(rannum>=1000000){
                break;
            }
        }
        String rand=""+rannum;
        return rand;
    }

    public static String getAuthCode(){
        Random random=new Random();
        int rannum= (int)(random.nextDouble()*(10000000));
        for (;;){
            rannum= (int)(random.nextDouble()*(1000000));
            if(rannum>=100000){
                break;
            }
        }
        String rand=""+rannum;
        return rand;
    }


    public static String  getSort(String userId,String type){  //每日获取用户排序表的id （日期加用户id）
        Date date=new Date();
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
        String dates=dateFormatYYYYMMDD.format(date);
        return dates+userId+type;
    }



    public static  void main(String [] a){
        System.out.println(getaccount());

//        int [] da=new int[]{0,1,2,3,4,5,6,7,8,9};
//        String account="";
//        int dd=0;
//        for(int a1=0;a1<da.length;a1++){
//            for(int a2=0;a2<da.length;a2++){
//                for(int a3=0;a3<da.length;a3++){
//                                        dd=dd+1;
//                                        account=da[a1]+""+da[a2]+""+da[a3];
//                                        System.out.println(account);
//                }
//            }
//        }
//        System.out.println("dd"+dd);
//    for (int i=0;i<1000;i++){
//        Random random=new Random();
//        double aa=random.nextDouble();
//        for(;;){
//            aa=random.nextDouble();
//            if(aa*100>10){
//                break;
//            }
//        }
//        int rannum= (int)(aa*(10000000));
//        System.out.println(rannum+">>"+aa+">>>"+i);
//        String rand="9"+rannum;
//        System.out.println(rand);
//        if(rannum<1000000){
//            break;
//        }
//
//    }


    }

}
