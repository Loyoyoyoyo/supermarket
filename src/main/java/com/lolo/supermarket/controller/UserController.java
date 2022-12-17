package com.lolo.supermarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.service.UserService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import com.lolo.supermarket.util.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
public class UserController {

    @Resource
    UserService userService;
    @Resource
    UserMapper userMapper;

    //注册
    @PostMapping("/sign-up")
    public Result signUp(@RequestBody User user) {
        //为空
        if (user.getEmail() == null ||
                user.getPassword() == null||
                user.getUsername() == null ||
                user.getPhone() == null){

            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        //邮箱格式
        if (!Valid.isValidEmail(user.getEmail())) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = userService.signUp(user);
        //用户已存在
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.USER_ERROR2.getCode(),
                    ResultEnum.USER_ERROR2.getMes());
        }

        return ResultGenerator.success();

    }

    //登录
    @PostMapping("/sign-in")
    public Result signIn(@RequestBody User user, HttpServletRequest req) {
        //为空
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }

        int result = userService.signIn(user);

        //用户不存在
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.USER_ERROR.getCode(),
                    ResultEnum.USER_ERROR.getMes());
        }
        //密码错误
        if (result == 1) {
            return ResultGenerator.fail(ResultEnum.PASS_ERROR.getCode(),
                    ResultEnum.PASS_ERROR.getMes());
        }
        //给cookie
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",user.getEmail());
        User user1 = userMapper.selectOne(queryWrapper);
        req.getSession().setAttribute("user",user1);
        return ResultGenerator.success();

    }

    @PostMapping("/re-pass")
    public Result rePass(@RequestBody User user) {
        //为空
        if (user.getEmail() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = userService.rePass(user);
        //用户不存在
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.USER_ERROR.getCode(),
                    ResultEnum.USER_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }


}
