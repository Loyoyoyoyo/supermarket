package com.lolo.supermarket.common;

public enum ResultEnum {
    success(10000, "成功"),
    LOGIN_ERROR(20000, "登录失败"),
    PARAM_ERROR(0, "参数错误"),
    PASS_ERROR(00, "密码错误"),
    STOCK_ERROR(000, "商品库存不足"),
    USER_ERROR(-1, "用户不存在"),
    TYPE_EROOR(-11, "商品类别不存在"),
    GOOD_ERROR(-111, "商品不存在"),
    ORDER_ERROR(-1111, "订单不存在"),
    ACTIVI_ERROR(-11111,"活动不存在"),
    CAR_ERROR(-11111, "购物车不存在该商品"),
    USER_ERROR2(1, "用户已存在"),
    GOOD_ERROR2(11, "商品已存在"),
    ACTIVI_ERROR2(111,"活动已存在"),
    ACTIVI_ERROR3(1111,"该类商品已有同类型活动");

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
