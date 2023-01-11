package com.lolo.supermarket.util;


import org.apache.shiro.crypto.hash.Md5Hash;

public class ShiroMD5 {
    public static String ShiroMD5Hash(String password){
        Md5Hash md5Hash = new Md5Hash(password,"salt",3);
        return md5Hash.toHex();
    }
}
