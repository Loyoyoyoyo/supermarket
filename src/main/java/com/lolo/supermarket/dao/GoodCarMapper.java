package com.lolo.supermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.GoodCar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GoodCarMapper extends BaseMapper<GoodCar> {
    @Select("SELECT name FROM role WHERE id IN(\n" +
            "\tSELECT rid FROM user_role WHERE uid =(\n" +
            "\tSELECT id FROM user WHERE username = #{principal}))")
    List<String> goodtypeByCarMapper(@Param("principal") String principal);
}
