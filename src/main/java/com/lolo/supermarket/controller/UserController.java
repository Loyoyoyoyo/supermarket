package com.lolo.supermarket.controller;

import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.service.UserService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import com.lolo.supermarket.util.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user) {
        //为空
        if (user.getEmail() == null ||
                user.getPassword() == null||
                user.getUsername() == null ||
                user.getPhone() == null){

            return ResultGenerator.fail(ResultEnum.EMPTY_ERROR.getCode(),
                    ResultEnum.EMPTY_ERROR.getMes());
        }
        //邮箱格式
        if (!Valid.isValidEmail(user.getEmail())) {
            return ResultGenerator.fail(ResultEnum.EMAIL_ERROR.getCode(),
                    ResultEnum.EMAIL_ERROR.getMes());
        }
        int result = userService.signUp(user);
        //用户已存在
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.USER_ERROR2.getCode(),
                    ResultEnum.USER_ERROR2.getMes());
        }

        return ResultGenerator.success();

    }

    @PostMapping("/signIn")
    public Result signIn(@RequestBody User user) {
        int result = userService.signIn(user);
        //为空
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResultGenerator.fail(ResultEnum.EMPTY_ERROR.getCode(),
                    ResultEnum.EMPTY_ERROR.getMes());
        }
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
        return ResultGenerator.success();

    }

    @PostMapping("/rePass")
    public Result rePass(@RequestBody User user) {
        //为空
        if (user.getEmail() == null) {
            return ResultGenerator.fail(ResultEnum.EMPTY_ERROR.getCode(),
                    ResultEnum.EMPTY_ERROR.getMes());
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
