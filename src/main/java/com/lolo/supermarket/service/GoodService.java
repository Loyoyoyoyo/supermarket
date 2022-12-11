package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.bean.Goods;
import com.lolo.supermarket.dao.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    GoodsMapper goodsMapper;

    public Goods[] selectAll(){
        String[] types = {"女装", "男装","手机","美妆"};
        Goods[] result = new Goods[8];
        int i = 0;
        for (String type: types) {
            QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("good_type",type)
                    .orderByDesc("weight")
                    .orderByAsc("good_create_time");
            List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
            result[i] = goodList.get(0);
            result[i+1] = goodList.get(1);
            i+=2;
        }
        return result;
    }

    public Goods selectById(int id){
//        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
//        // 选择type为某种类型的数据
//        userQueryWrapper.like("id",id);

        System.out.println((goodsMapper.selectById(id)));
        return goodsMapper.selectById(id);

    }

    public List<Goods> selectByType(String type){
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        // 选择type为某种类型的数据
        userQueryWrapper.like("good_type",type);
        // 按权重递减
        userQueryWrapper.orderByDesc("weight");
        List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
        return goodList;
    }

    public String addGood(Goods good) {
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        goodsMapper.insert(good);
        return "插入成功";
    }
    public String deleteById(int id){
        goodsMapper.deleteById(id);
        return "删除成功";
    }

    public String updateWeight(int id,int weight){
        Goods good = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("weight",weight)
                .eq("id",id);
        goodsMapper.update(good,updateWrapper);
        return "修改完成";
    }
    public String updateName(int id,String name){
        Goods goods = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("good_name",name).eq("id",id);
        goodsMapper.update(goods,updateWrapper);
        return "修改商品名称成功";
    }
}
