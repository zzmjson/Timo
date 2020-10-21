package com.linln.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Dateline {

    /**
     *  java 获取 获取某年某月 所有日期（yyyy-mm-dd格式字符串）
     * @param year
     * @param month
     * @return
     */
    public List<String> getMonthFullDay(int year , int month){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month-1 );
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH,1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count ; j++) {
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        return fullDayList;
    }



    /**
     * 获取某年某月某日 是 星期几
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getDayOfWeek(int year , int month , int day){
        //本地化
        Locale.setDefault(Locale.CHINA);
        String[] dayStringOfWeek = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        // Java中Calendar.DAY_OF_WEEK其实表示：一周中的第几天，所以他会受到第一天是 星期一 还是 星期日 的影响
        int d = cal.get(Calendar.DAY_OF_WEEK);
        return dayStringOfWeek[d-1];
    }


    public static String getDayOfWeek(int year , int month , int day,boolean trues){
        //本地化
        Locale.setDefault(Locale.CHINA);
        String[] dayStringOfWeek = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        // Java中Calendar.DAY_OF_WEEK其实表示：一周中的第几天，所以他会受到第一天是 星期一 还是 星期日 的影响
        int d = cal.get(Calendar.DAY_OF_WEEK);
        return dayStringOfWeek[d-1];
    }



    public static List<Integer> printDate(Date dt)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);//放入Date类型数据
        List<Integer> listString=new ArrayList<>();
        listString.add( (calendar.get(Calendar.YEAR)));  // 获取年份c
        listString.add( (calendar.get(Calendar.MONTH))+1);//获取月份
        listString.add( (calendar.get(Calendar.DATE)));   // 获取日
        listString.add( (calendar.get(Calendar.HOUR_OF_DAY)));//时（24小时制）
        listString.add((calendar.get(Calendar.MINUTE)));//分
        listString.add((calendar.get(Calendar.SECOND)));//秒
        return listString;
    }




    //获取两个时间日期的间隔年月日，时分秒
    public static String getTime(Date currentTime,Date firstTime){

        long diff = currentTime.getTime() - firstTime.getTime();//这样得到的差值是微秒级别

        Calendar  currentTimes =dataToCalendar(currentTime);//当前系统时间转Calendar类型

        Calendar  firstTimes =dataToCalendar(firstTime);//查询的数据时间转Calendar类型

        int year = currentTimes.get(Calendar.YEAR) - firstTimes.get(Calendar.YEAR);//获取年

        int month = currentTimes.get(Calendar.MONTH) - firstTimes.get(Calendar.MONTH);

        int day = currentTimes.get(Calendar.DAY_OF_MONTH) - firstTimes.get(Calendar.DAY_OF_MONTH);

        if (day < 0) {

            month -= 1;

            currentTimes.add(Calendar.MONTH, -1);

            day = day + currentTimes.getActualMaximum(Calendar.DAY_OF_MONTH);//获取日

        }

        if (month < 0) {

            month = (month + 12) % 12;//获取月

            year--;

        }

        long days = diff / (1000 * 60 * 60 * 24);

        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60); //获取时

        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);//获取分钟

        long s=(diff/1000-days*24*60*60-hours*60*60-minutes*60);//获取秒

        //年月日时分秒


        String CountTime=year+"/"+month+"/"+day+"-"+hours+":"+minutes+":"+s+"";

        return CountTime;

    }



    //Date类型转Calendar类型

    public static Calendar dataToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        return calendar;

    }


    public static String getStringTimestamp(Date date) {
        String timestamp = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long longTime = date.getTime();
            timestamp = Long.toString(longTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static Integer StringToTimestamp(String time){

        int times = 0;
        try {
            times = (int) ((Timestamp.valueOf(time).getTime())/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(times==0){
            System.out.println("String转10位时间戳失败");
        }
        return times;

    }




    public static  void main(String [] a){
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        List<Integer> listdate=printDate(date);
//        System.out.println(listdate.get(0)+listdate.get(1)+listdate.get(2));
        System.out.println(getStringTimestamp(date));
    }

}
