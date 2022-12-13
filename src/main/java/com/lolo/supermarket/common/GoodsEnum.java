package com.lolo.supermarket.common;

public enum GoodsEnum {
    success(10000,"成功"),
    EMPTY_ERROR(0,"输入为空"),
    USER_ERROR(-1,"用户不存在"),
    USER_ERROR2(-2,"用户已存在"),
    EMAIL_ERROR(-3,"邮箱格式错误"),
    PASS_ERROR(1,"密码错误");

    private int code;
    private String mes;

    GoodsEnum() {
    }

    GoodsEnum(int code, String mes) {
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
