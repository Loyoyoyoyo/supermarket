package com.lolo.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
