package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private String goodType;
    private String goodName;
    private int stock;
    @TableField(fill = FieldFill.INSERT)
    private Date goodCreateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date goodUpdateTime;
    private int weight;

}
