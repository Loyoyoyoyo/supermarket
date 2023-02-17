package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class GoodCarSum {
    private Integer userId;
    private Integer goodNum;
    // 判断是否曾经算过购物车总价
    private Integer bool_sum;
    private Double sum;

    public GoodCarSum(Integer bool_sum, double sum) {

        this.bool_sum = bool_sum;
        this.sum = sum;
    }
}
