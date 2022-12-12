package com.lolo.supermarket.common;

public enum GoodsEnum {
    success(10000,"成功"),
    UNKNOWN_ERROR(-1,"未知错误");

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
