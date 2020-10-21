package com.linln.common.common;

public class CashierResultUtil {
    public static CashierResult success(String object){
        CashierResult result=new CashierResult();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }
    public static CashierResult success() {
        return success(null);
    }
    public static CashierResult error(String msg) {
        CashierResult result = new CashierResult();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
