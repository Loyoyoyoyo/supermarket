package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("orders")
public class Orders {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private int userId;
    private int goodId;
    private int goodNum;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
