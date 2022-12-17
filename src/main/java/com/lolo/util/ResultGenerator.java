package com.lolo.util;

import com.lolo.common.ResultEnum;


public class ResultGenerator {

    //成功无数据
    public static Result success(){
        Result result = new Result();
        result.setResultCode(ResultEnum.success.getCode());
        result.setMessage(ResultEnum.success.getMes());
        return result;
    }
    //成功无数据，自定义信息
    public static Result successMes(int id) {
        Result result = new Result();
        result.setResultCode(ResultEnum.success.getCode());
        result.setMessage("添加的商品id为" + id);
        return result;
    }
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
}
