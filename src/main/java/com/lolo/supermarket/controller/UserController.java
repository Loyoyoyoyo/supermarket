package com.lolo.supermarket.controller;


import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.service.UserService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import com.lolo.supermarket.util.Valid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    UserService userService;

    //注册
    @PostMapping("/sign-up")
    public Result signUp(@RequestBody User user) {
        //为空
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getUsername() == null ||
                user.getPhone() == null) {

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

    //用户名登录
    @PostMapping("/sign-in-by-name")
    public Result signInByName(@RequestBody User user) {
        //1 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        //2 封装请求数据到 token 对象中
        AuthenticationToken token = new
                UsernamePasswordToken(user.getUsername(), user.getPassword());
        //3 调用 login 方法进行登录认证
        try {
            subject.login(token);
            return ResultGenerator.successMes("登录成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return ResultGenerator.fail(ResultEnum.USER_ERROR.getCode(), ResultEnum.USER_ERROR.getMes());
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return ResultGenerator.fail(ResultEnum.PASS_ERROR.getCode(), ResultEnum.PASS_ERROR.getMes());
        }
    }

    // 邮箱登录
    @PostMapping("/sign-in-by-email")
    public Result signInByEmail(@RequestBody User user) {
        //1 获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        //2 封装请求数据到 token 对象中
        AuthenticationToken token = new
                UsernamePasswordToken(user.getEmail(), user.getPassword());
        //3 调用 login 方法进行登录认证
        try {
            subject.login(token);
            return ResultGenerator.successMes("登录成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return ResultGenerator.fail(ResultEnum.USER_ERROR.getCode(), ResultEnum.USER_ERROR.getMes());
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return ResultGenerator.fail(ResultEnum.PASS_ERROR.getCode(), ResultEnum.PASS_ERROR.getMes());
        }
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
