package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String goodType;
    private String goodName;
    private String goodBrand;
    private Integer stock;
    private Double price;
    private Double activityPrice;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Integer weight;

}
