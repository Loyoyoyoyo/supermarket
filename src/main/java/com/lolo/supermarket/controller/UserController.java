package com.lolo.supermarket.controller;

import com.lolo.supermarket.common.GoodsEnum;
import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.service.UserService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import com.lolo.supermarket.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lolo.supermarket.common.GoodsEnum;

import javax.annotation.Resource;


@RestController
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user) {
        //邮箱格式
        if(!Valid.isValidEmail(user.getEmail())){
            return ResultGenerator.fail(GoodsEnum.EMAIL_ERROR.getCode(),
                    GoodsEnum.EMAIL_ERROR.getMes());
        }
        int result = userService.signUp(user);
        //用户已存在
        if(result == -1){
            return ResultGenerator.fail(GoodsEnum.USER_ERROR2.getCode(),
                    GoodsEnum.USER_ERROR2.getMes());
        }

        return ResultGenerator.success();

    }

    @PostMapping("/signIn")
    public Result signIn(@RequestBody User user) {
        int result = userService.signIn(user);
        //不存在该用户
        if (result == -1) {
            return ResultGenerator.fail(GoodsEnum.USER_ERROR.getCode(),
                    GoodsEnum.USER_ERROR.getMes());
        }
        //密码错误
        if (result == 1){
            return ResultGenerator.fail(GoodsEnum.PASS_ERROR.getCode(),
                    GoodsEnum.PASS_ERROR.getMes());
        }
        return ResultGenerator.success();

    }

}
