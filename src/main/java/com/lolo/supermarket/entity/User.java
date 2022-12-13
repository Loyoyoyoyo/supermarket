package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    @TableId
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;

}
