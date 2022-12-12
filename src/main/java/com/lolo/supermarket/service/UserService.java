package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public String signUp(User user) {
        userMapper.insert(user);
        return "ok";
    }
}
