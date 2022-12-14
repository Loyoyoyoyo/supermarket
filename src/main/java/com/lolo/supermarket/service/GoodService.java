package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.entity.Goods;
import com.lolo.supermarket.dao.GoodsMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodService {
    @Resource
    GoodsMapper goodsMapper;

    public Goods[] selectAll() {
        String[] types = {"女装", "男装", "手机", "美妆"};
        Goods[] result = new Goods[8];
        int i = 0;
        for (String type : types) {
            QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("good_type", type)
                    .orderByDesc("weight")
                    .orderByAsc("good_create_time");
            List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
            result[i] = goodList.get(0);
            result[i + 1] = goodList.get(1);
            i += 2;
        }
        return result;
    }

    public Goods selectById(Goods goods) {

        return goodsMapper.selectById(goods.getId());

    }

    /**
     * 按种类获取商品，并且按权重进行排序
     */
    public List<Goods> selectByType(Goods goods) {
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        // 选择type为某种类型的数据
        userQueryWrapper.like("good_type", goods.getGoodType());
        // 按权重递减
        userQueryWrapper.orderByDesc("weight");
        List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
        return goodList;
    }

    //8.可以按名称搜索商品，并且按权重排序。支持 按种类搜索、不分种类搜索 两种方式
    public List<Goods> selectByName(String name, Object type) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("good_name", name).orderByDesc("weight");

        if (type != null ) {
            queryWrapper.like("good_type", type);
        }

        return goodsMapper.selectList(queryWrapper);
    }

    public String addGood(Goods good) {
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        goodsMapper.insert(good);
        return "ok";
    }

    public void deleteById(Goods goods) {
        goodsMapper.deleteById(goods.getId());
        return;
    }

    public String updateWeight(Goods goods) {
        Goods good = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("weight", goods.getWeight())
                .eq("id", goods.getId());
        goodsMapper.update(good, updateWrapper);
        return "ok";
    }

    public String updateName(Goods goods) {
        Goods goods1 = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("good_name", goods.getGoodName()).eq("id", goods.getId());
        goodsMapper.update(goods1, updateWrapper);
        return "ok";
    }

    /**
     * 修改库存
     */
    public void updateStock(Goods goods){
        Goods goods1 = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("stock", goods.getStock()).eq("id", goods.getId());
        goodsMapper.update(goods1, updateWrapper);

    }

}
