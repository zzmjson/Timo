package com.linln.common.utils;

import java.text.NumberFormat;

public class NumberFormatUtil {
    //元转换为分
    public static String parseYuan(String amount){
        NumberFormat format = NumberFormat.getInstance();
        try{
            Number number = format.parse(amount);
            double temp = number.doubleValue() * 100.0;
            format.setGroupingUsed(false);
            // 设置返回数的小数部分所允许的最大位数
            format.setMaximumFractionDigits(0);
            amount = format.format(temp);
        } catch (Exception e){
            e.getMessage();
        }
        return amount;
    }

    //分转换为元
    public static String parseFen(String amount){
        NumberFormat format = NumberFormat.getInstance();
        try{
            Number number = format.parse(amount);
            double temp = number.doubleValue() / 100.0;
            format.setGroupingUsed(false);
            // 设置返回的小数部分所允许的最大位数
            format.setMaximumFractionDigits(2);
            amount = format.format(temp);
        } catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }

    public static  void main(String [] a){
        System.out.println(parseYuan("50"));
    }
}
