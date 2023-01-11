package com.lolo.supermarket.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5main {
    public static void main(String[] args) {
        String password= "666666";
        Md5Hash md5Hash = new Md5Hash(password,"salt",3);
        System.out.println(md5Hash.toHex());
    }
}
