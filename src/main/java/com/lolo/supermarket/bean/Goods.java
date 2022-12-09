package com.lolo.supermarket.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @TableId
    private int id;
    private String goodType;
    private String goodName;
    @TableField(fill = FieldFill.INSERT)
    private Date goodCreateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date goodUpdateTime;



}
