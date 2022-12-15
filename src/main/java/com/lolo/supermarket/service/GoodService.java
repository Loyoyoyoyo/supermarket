package com.lolo.supermarket.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.controller.GoodsController;
import com.lolo.supermarket.entity.Goods;
import com.lolo.supermarket.dao.GoodsMapper;
import com.lolo.supermarket.dao.GoodCarMapper;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.entity.UserCar;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.QueryEval;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class GoodService {
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodCarMapper goodCarMapper;
    @Resource
    HttpServletRequest httpServletRequest;

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

        if (type != null) {
            queryWrapper.like("good_type", type);
        }

        return goodsMapper.selectList(queryWrapper);
    }

    public String addGood(Goods good) {
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
    public void updateStock(Goods goods) {
        Goods goods1 = goodsMapper.selectById(goods.getId());
        goods1.setStock(goods.getStock());
        goodsMapper.updateById(goods1);
    }

    /**
     * 库存-1
     *
     * @param userCar
     */
    public void cutStock(UserCar userCar, int num) {
        Goods good = goodsMapper.selectById(userCar.getGoodId());
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("stock", good.getStock() + num)
                .eq("id", userCar.getGoodId());
        goodsMapper.update(good, updateWrapper);
    }

    /**
     * 添加进购物车
     */
    public int createCarGood(UserCar userCar) {
        //判断库存
        int stock = goodsMapper.selectById(userCar.getGoodId()).getStock();
        if (stock == 0) {
            return -1;
        }

        QueryWrapper<UserCar> queryWrapper = new QueryWrapper<>();
        //session获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //用户id+商品id筛选userCar1
        queryWrapper.eq("user_id", user.getId())
                .eq("good_id", userCar.getGoodId());
        UserCar userCar1 = goodCarMapper.selectOne(queryWrapper);
        //已存在，就增加数量
        if (userCar1 != null) {
            userCar1.setGoodNum(userCar1.getGoodNum() + 1);
            goodCarMapper.updateById(userCar1);
            return 1;
        //不存在，就新建
        } else {
            userCar.setUserId(user.getId());
            userCar.setGoodNum(1);
            goodCarMapper.insert(userCar);
            return 1;
        }
    }

    /**
     * 修改购物车内商品的数量
     */
    public boolean updateCarGoodNum(UserCar userCar) {
        //session获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //获取用户购物车该商品的原本数量
        QueryWrapper<UserCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId())
                .eq("good_id", userCar.getGoodId());
        UserCar userCar1 = goodCarMapper.selectOne(queryWrapper);
        // 1.增加购物车数量
        if (userCar.getGoodNum() > userCar1.getGoodNum()) {
            //判断库存
            int stock = goodsMapper.selectById(userCar.getGoodId()).getStock();
            //库存不足
            if (stock < userCar.getGoodNum()) {
                return false;
            } else {
                userCar1.setGoodNum(userCar.getGoodNum());
                goodCarMapper.updateById(userCar1);
                return true;
            }
            //2不变、减少购物车数量
        } else {
            userCar1.setGoodNum(userCar.getGoodNum());
            goodCarMapper.updateById(userCar1);
            return true;
        }
    }
}