package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_car")
public class GoodCar {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer goodId;
    private Integer goodNum;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Double sum;

    public GoodCar() {
    }

    public GoodCar(Integer userId, Integer goodNum, Double sum) {
        this.userId = userId;
        this.goodNum = goodNum;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "GoodCar{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodId=" + goodId +
                ", goodNum=" + goodNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", sum=" + sum +
                '}';
    }
}
