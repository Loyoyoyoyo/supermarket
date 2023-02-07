package com.lolo.supermarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("activity_good")
public class ActivityGood {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer gid;
    private Integer aid;

    public ActivityGood() {
    }

    public ActivityGood(Integer aid,Integer gid) {
        this.gid = gid;
        this.aid = aid;
    }
}
