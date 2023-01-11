package com.lolo.supermarket.util;

import com.lolo.supermarket.common.ResultEnum;


public class ResultGenerator {

    //成功无数据
    public static Result success(){
        Result result = new Result();
        result.setResultCode(ResultEnum.success.getCode());
        result.setMessage(ResultEnum.success.getMes());
        return result;
    }
    //成功无数据，自定义信息
    public static Result successMes(String mes) {
        Result result = new Result();
        result.setResultCode(ResultEnum.success.getCode());
        result.setMessage(mes);
        return result;
    }
    //
    //成功有数据
    public static Result successData(Object data){
        Result result = new Result();
        result.setResultCode(ResultEnum.success.getCode());
        result.setMessage(ResultEnum.success.getMes());
        result.setData(data);
        return result;
    }

    //失败无数据
    public static Result fail(int code,String message ){
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
    //失败无数据且自定义message
    public static Result failMes(int code,String mes) {
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(mes);
        return result;
    }
    }

