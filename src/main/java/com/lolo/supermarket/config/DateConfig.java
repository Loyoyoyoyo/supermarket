package com.lolo.supermarket.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("goodCreateTime",new Date(),metaObject);
        this.setFieldValByName("goodUpdateTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("goodUpdateTime",new Date(),metaObject);
    }
}
