package com.lolo.supermarket.util;

import com.lolo.supermarket.common.GoodsEnum;


public class ResultGenerator {

    //成功无数据
    public static Result success(){
        Result result = new Result();
        result.setResultCode(GoodsEnum.success.getCode());
        result.setMessage(GoodsEnum.success.getMes());
        return result;
    }
    //成功无数据，自定义信息
    public static Result successMes(int id) {
        Result result = new Result();
        result.setResultCode(GoodsEnum.success.getCode());
        result.setMessage("添加的商品id为" + id);
        return result;
    }
    //成功有数据
    public static Result successData(Object data){
        Result result = new Result();
        result.setResultCode(GoodsEnum.success.getCode());
        result.setMessage(GoodsEnum.success.getMes());
        result.setData(data);
        return result;
    }

    //失败无数据
    public static Result fail(){
        Result result = new Result();
        result.setResultCode(GoodsEnum.success.getCode());
        result.setMessage(GoodsEnum.success.getMes());
        return result;
    }
    //失败无数据且自定义message
}
