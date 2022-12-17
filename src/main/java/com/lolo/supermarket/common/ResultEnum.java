package com.lolo.supermarket.common;

public enum ResultEnum {
    success(10000,"成功"),
    PARAM_ERROR(0,"参数错误"),
    USER_ERROR(-1,"用户不存在"),
    USER_ERROR2(-2,"用户已存在"),
    PASS_ERROR(-3,"密码错误"),
    GOOD_ERROR(-11,"商品不存在"),
    GOOD_ERROR2(-22,"商品已存在"),
    TYPE_EROOR(-33,"商品类别不存在"),
    STOCK_ERROR(-44,"商品库存不足"),
    CAR_ERROR(-55,"购物车不存在该商品");


    private int code;
    private String mes;

    ResultEnum() {
    }

    ResultEnum(int code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
