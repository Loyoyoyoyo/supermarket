package com.lolo.supermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.GoodCar;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface GoodCarMapper extends BaseMapper<GoodCar> {
}
