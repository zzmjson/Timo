package com.linln.common.utils;

import java.util.Date;

public class DatePoorUtil {
    public static long getDatePoor(Date endDate, Date nowDate) {
        long between=((endDate.getTime()-nowDate.getTime())/1000)/60;
        return between/60;
    }
}
