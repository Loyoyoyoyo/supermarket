package com.lolo.supermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.UserCar;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodCarMapper extends BaseMapper<UserCar> {
}
