package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lolo.supermarket.bean.Goods;
import com.lolo.supermarket.dao.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    GoodsMapper goodsMapper;

    public Goods selectById(int id){
        Goods goods = goodsMapper.selectById(id);
        return goods;
    }

    public List<Goods> selectByType(String type){
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("good_type",type);
        List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
        return goodList;
    }
}
