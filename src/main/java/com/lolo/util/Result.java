package com.lolo.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T>{
    private String message;
    private int resultCode;
    private T data;



}
