package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("activity")
public class Activity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //在时间属性上面加上该注解
    private Date beginTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //在时间属性上面加上该注解
    private Date endTime;
    private String type;
    private String goodBrand;
    private String goodType;
    private int status;
}
