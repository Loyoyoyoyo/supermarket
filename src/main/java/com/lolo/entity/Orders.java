package com.lolo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("orders")
public class Orders {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer  id;
    private Integer  userId;
    private Integer  userOrderId;
    private Integer  goodId;
    private Integer  goodNum;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
