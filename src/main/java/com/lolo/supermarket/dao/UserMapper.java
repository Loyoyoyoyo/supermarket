package com.lolo.supermarket.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
