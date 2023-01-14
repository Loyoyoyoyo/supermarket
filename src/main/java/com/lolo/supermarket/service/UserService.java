package com.lolo.supermarket.service;
import com.lolo.supermarket.util.ShiroMD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    /**
     * 根据名字获取用户
     */
    public User getUserByName(String name){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",name);
        return userMapper.selectOne(queryWrapper);
    }
    /**
     * 根据邮箱获取用户
     */
    public User getUserByEmail(String email){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据用户获取角色
     */
    public List<String> RoleInfoByUser(String username){
        return userMapper.RoleInfoByUserMapper(username);
    }



    /**
     * 注册
     * @param user
     * @return
     */
    public int signUp(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //邮箱存在
        queryWrapper.eq("email",user.getEmail());
        if(userMapper.selectOne(queryWrapper)!= null){
            return -1;
        }
        user.setPassword(ShiroMD5.ShiroMD5Hash(user.getPassword()));
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
        queryWrapper.eq("email",user.getEmail());
        User user1 = userMapper.selectOne(queryWrapper);
        // 不存在该用户
        if(user1 == null){
            return -1;
        }else{
            // 密码正确
            if(user1.getPassword().equals(user.getPassword())){
                return 0;
                // 密码错误
            }else{
                return 1;
            }
        }
    }

    /**
     * 重置密码
     */
    public int rePass(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user1 = new User();
        //用户不存在
        queryWrapper.eq("email",user.getEmail());
        if(userMapper.selectOne(queryWrapper) == null){
            return -1;
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password",user.getPassword())
                .eq("email",user.getEmail());
        userMapper.update(user1,updateWrapper);
        return 0;
    }

}
