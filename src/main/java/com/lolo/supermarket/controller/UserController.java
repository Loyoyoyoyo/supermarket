package com.lolo.supermarket.controller;

import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.service.UserService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user) {
        userService.signUp(user);
        return ResultGenerator.success();
    }
}
