package com.lolo.supermarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lolo.supermarket.bean.Goods;
import com.lolo.supermarket.dao.GoodsMapper;
import com.lolo.supermarket.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    GoodService goodService;

    @GetMapping("/retrieve")
    public Goods testController(@RequestParam("id") int id){
        return goodService.selectById(id);
    }

    @GetMapping("/type")
    public List<Goods> testType(@RequestParam("type") String type){
        return goodService.selectByType(type);
    }

}
