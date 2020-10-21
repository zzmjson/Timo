package com.linln.common.common;

/**
 * Created by HSY on 2018/9/5.
 */
public class ResultUtil {
    public static Result success(Object object){
        Result result=new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }
    public static Result successMsg(String msg){
        Result result=new Result();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }
    public static Result successPage(Long total,Object rows){
        Result result=new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setTotal(total);
        result.setRows(rows);
        return result;
    }
    public static Result success() {
        return success(null);
    }
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
