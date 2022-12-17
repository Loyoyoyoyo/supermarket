package com.lolo.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String goodType;
    private String goodName;
    private Integer stock;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Integer weight;

}
