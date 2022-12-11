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

    //3.商品需要按权重在首页进行展示，每类商品展示权重前2的，相同权重则优先选取创建时间晚的
    @GetMapping("/selectAll")
    public Goods[] selectAll(){
        return goodService.selectAll();
    }

    //按id查询
    @GetMapping("/retrieveId")
    public Goods retrieveId(@RequestParam("id") int id){
        Goods good = goodService.selectById(id);
        return good;
    }


    //查询
    //按种类获取商品，并且按权重进行排序
    @GetMapping("/retrieveType")
    public List<Goods> retrieveType(@RequestParam("type") String type){
        return goodService.selectByType(type);
    }

    //创建商品
    @PostMapping("/create")
    public String create(Goods goods){
        return goodService.addGood(goods);
    }

    //删除商品
    @GetMapping("/deleteById")
    public String deleteById(@RequestParam("id") int id){
       return goodService.deleteById(id);
    }

    //修改商品权重
    @GetMapping("/updateWeight")
    public String updateWeight(@RequestParam("id") int id,
                               @RequestParam("weight") int weight){
        return goodService.updateWeight(id,weight);

    }
    //修改商品名称
    @GetMapping("/updateName")
    public String updateName(@RequestParam("id") int id,
                             @RequestParam("name") String name){
        return goodService.updateName(id, name);
    }
}
