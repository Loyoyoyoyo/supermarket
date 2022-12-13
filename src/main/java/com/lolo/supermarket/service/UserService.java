package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    public int signUp(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //邮箱存在
        if(queryWrapper.select("email",user.getEmail())!= null){
            return -1;
        }

        userMapper.insert(user);
        return 0;
    }

    /**
     * 登录
     * @param user
     * @return
     */

    public int signIn(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("email",user.getEmail());
        User user1 = userMapper.selectOne(queryWrapper);
        // 不存在该用户
        if(user1 == null){
            return -1;
        }else{
            // 密码正确
            if(user1.getPassword()==user.getPassword()){
                return 0;
            // 密码错误
            }else{
                return 1;
            }
        }
    }
}
