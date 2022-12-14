package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
@Data
@TableName("user_car")
public class UserCar {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private int userId;
    private int goodId;
    private int goodNum;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
