package com.lolo.supermarket.controller;

import com.lolo.supermarket.entity.Goods;
import com.lolo.supermarket.service.GoodService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    GoodService goodService;

    //3.商品需要按权重在首页进行展示，每类商品展示权重前2的，相同权重则优先选取创建时间晚的
    @GetMapping("/retrieve-all")
    public Result retrieveAll() {
        Goods[] data =  goodService.selectAll();
        return ResultGenerator.successData(data);
    }

    //按id查询
    @PostMapping("/retrieve-id")
    public Result retrieveId(@RequestParam int id) {
        Goods data = goodService.selectById(id);
        return ResultGenerator.successData(data);
    }


    //查询
    //按种类获取商品，并且按权重进行排序
    @GetMapping("/retrieve-type")
    public Result retrieveType(@RequestParam String type) {
        List<Goods> data = goodService.selectByType(type);
        return ResultGenerator.successData(data);
    }

    //8.可以按名称搜索商品，并且按权重排序。支持 按种类搜索、不分种类搜索 两种方式String name, boolean byType, String type
    @PostMapping("/retrieve-name")
    public Result retrieveByName(@RequestBody Goods goods) {
        List<Goods> data = goodService.selectByName(goods.getGoodName(), goods.getGoodType());
        return ResultGenerator.successData(data);
    }

    //创建商品
    @PostMapping("/create")
    public Result create(@RequestBody Goods goods) {
        goodService.addGood(goods);
        int id = goods.getId();
        return ResultGenerator.successMes(id);
    }

    //删除商品
    @GetMapping("/delete-id")
    public Result deleteById(@RequestBody int id) {
        goodService.deleteById(id);
        return ResultGenerator.success();
    }

    //修改商品权重
    @GetMapping("/update-weight")
    public Result updateWeight(@RequestBody int id,
                               @RequestBody int weight) {
        return ResultGenerator.success();

    }

    //修改商品名称
    @GetMapping("/update-name")
    public Result updateName(@RequestBody int id,
                             @RequestBody String name) {
        return ResultGenerator.success();
    }
}
