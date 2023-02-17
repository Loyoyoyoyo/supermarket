package com.lolo.supermarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.dao.ActivityMapper;
import com.lolo.supermarket.dao.GoodCarMapper;
import com.lolo.supermarket.dao.GoodsMapper;
import com.lolo.supermarket.dao.OrdersMapper;
import com.lolo.supermarket.entity.*;
import com.lolo.supermarket.exception.NotEnoughException;
import com.lolo.supermarket.service.GoodService;
import org.apache.shiro.SecurityUtils;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service("goodService")
public class GoodServiceImpl implements com.lolo.supermarket.service.GoodService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    GoodCarMapper goodCarMapper;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    ActivityMapper activityMapper;


    /**
     * @return
     */
    @Override
    public Goods[] selectAll() {
        String[] types = {"女装", "男装", "手机", "美妆"};
        Goods[] result = new Goods[8];
        int i = 0;
        for (String type : types) {
            QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("good_type", type)
                    .orderByDesc("weight")
                    .orderByAsc("create_time");
            List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
            result[i] = goodList.get(0);
            result[i + 1] = goodList.get(1);
            i += 2;
        }
        return result;
    }

    /**
     * 按id查商品
     *
     * @param goods
     * @return
     */
    @Override
    public Goods selectById(Goods goods) {
        // 商品不存在 只在service查一次搞定
        Goods good = goodsMapper.selectById(goods.getId());
        return good;

    }

    /**
     * 按种类查商品，并且按权重进行排序
     */
    @Override
    public List<Goods> selectByType(Goods goods) {
        QueryWrapper<Goods> userQueryWrapper = new QueryWrapper<>();
        // 选择type为某种类型的数据
        userQueryWrapper.like("good_type", goods.getGoodType());
        // 按权重递减
        userQueryWrapper.orderByDesc("weight");
        List<Goods> goodList = goodsMapper.selectList(userQueryWrapper);
        return goodList;
    }


    /**
     * 可以按名称搜索商品，并且按权重排序。支持 按种类搜索、不分种类搜索 两种方式
     *
     * @param
     * @param
     * @return
     */
    @Override
    public List<Goods> selectByName(GoodRetrieveName goodRetrieveName) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("good_name", goodRetrieveName.getGoodName()).orderByDesc("weight");


        if (goodRetrieveName.isByType() == true) {
            queryWrapper.like("good_type", goodRetrieveName.getGoodType());
        }
        return goodsMapper.selectList(queryWrapper);
    }

    /**
     * 管理员可以添加商品
     *
     * @param good
     * @return
     */
    @Override
    public int addGood(Goods good) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("good_name", good.getGoodName())
                .like("good_brand", good.getGoodBrand());
        //商品已存在
        if (goodsMapper.selectOne(queryWrapper) != null) {
            return -1;
        }
        goodsMapper.insert(good);
        return 1;
    }

    /**
     * 管理员可以删除商品
     *
     * @param goods
     */
    @Override
    public int deleteById(Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return -1;
        }
        goodsMapper.deleteById(goods.getId());
        return 1;
    }


    /**
     * 管理员可以修改商品权重
     *
     * @param goods
     * @return
     */
    @Override
    public int updateWeight(Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return -1;
        }
        Goods good = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("weight", goods.getWeight())
                .eq("id", goods.getId());
        goodsMapper.update(good, updateWrapper);
        return 1;
    }

    /**
     * 管理员可以修改商品名称
     *
     * @param goods
     * @return
     */
    @Override
    public int updateName(Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return -1;
        }
        Goods goods1 = new Goods();
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("good_name", goods.getGoodName()).eq("id", goods.getId());
        goodsMapper.update(goods1, updateWrapper);
        return 1;
    }

    /**
     * 管理员可以增、减库存
     */
    @Override
    public int updateStock(Goods goods) {
        Goods goods1 = goodsMapper.selectById(goods.getId());
        // 商品不存在
        if (goods1 == null) {
            return -1;
        }
        goods1.setStock(goods.getStock());
        goodsMapper.updateById(goods1);
        return 1;

    }


    /**
     * 3.用户可以添加商品进购物车
     */
    @Override
    public int createCarGood(GoodCar goodCar, HttpServletRequest httpServletRequest) {
        //商品不存在
        if (goodsMapper.selectById(goodCar.getGoodId()) == null) {
            return -1;
        }
        //库存不足
        int stock = goodsMapper.selectById(goodCar.getGoodId()).getStock();
        if (stock == 0) {
            return -2;
        }
        QueryWrapper<GoodCar> queryWrapper = new QueryWrapper<>();
        //session获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //用户id+商品id筛选userCar1
        queryWrapper.eq("user_id", user.getId())
                .eq("good_id", goodCar.getGoodId());
        GoodCar goodCar1 = goodCarMapper.selectOne(queryWrapper);
        //购物车已存在，就增加数量
        if (goodCar1 != null) {
            goodCar1.setGoodNum(goodCar1.getGoodNum() + goodCar.getGoodNum());
            goodCarMapper.updateById(goodCar1);
            return 1;
            //购物车不存在，就新建
        } else {
            goodCar.setUserId(user.getId());
            goodCar.setGoodNum(goodCar.getGoodNum());
            goodCarMapper.insert(goodCar);
            return 1;
        }
    }

    /**
     * 修改购物车内商品的数量
     */
    @Override
    public int updateCarGoodNum(GoodCar goodCar, HttpServletRequest httpServletRequest) {

        //商品不存在
        if (goodsMapper.selectById(goodCar.getGoodId()) == null) {
            return -1;
        }

        //session获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //用户id+商品id筛选userCar1
        QueryWrapper<GoodCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId())
                .eq("good_id", goodCar.getGoodId());
        GoodCar goodCar1 = goodCarMapper.selectOne(queryWrapper);
        //购物车里不存在
        if (goodCar1 == null) {
            return -2;
        }


        // 1.增加购物车数量
        if (goodCar.getGoodNum() > goodCar1.getGoodNum()) {
            //判断库存
            int stock = goodsMapper.selectById(goodCar.getGoodId()).getStock();
            //库存不足
            if (stock < goodCar.getGoodNum()) {
                return -3;
            } else {
                goodCar1.setGoodNum(goodCar.getGoodNum());
                goodCarMapper.updateById(goodCar1);
                return 1;
            }
            //2不变、减少购物车数量
        } else {
            goodCar1.setGoodNum(goodCar.getGoodNum());
            goodCarMapper.updateById(goodCar1);
            return 1;
        }
    }

    /**
     * 计算购物车总价
     *
     * @param httpServletRequest
     * @return
     */

    @Override
    public GoodCarSum goodCarSum(HttpServletRequest httpServletRequest,List<GoodCar> goodCars) {
        double sum = 0;
        //是否曾经记录过总价，默认为没有
        Integer bool_sum = -1;
        GoodCar goodCarSum = new GoodCar();

        //获取满减活动
        QueryWrapper<Activity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("status", 2);
        queryWrapper1.eq("type", "满减");
        List<Activity> activities = activityMapper.selectList(queryWrapper1);
        Iterator iterator0 = activities.iterator();

        int goodNum = 0;
        // 1如果有满减活动，就看看购物车里有没有参加满减的商品以及是否满足金额
        if (activities != null) {
            // 活动迭代器
            while (iterator0.hasNext()) {
                Object next0 = iterator0.next();
                Activity activity = (Activity) next0;
                // 解析出两个数  满多少  减多少
                String[] strings = activity.getCalculate().split("-");
                int enough = Integer.parseInt(strings[0]);
                int cut = Integer.parseInt(strings[1]);
                double sum_list = 0;
                List<GoodCar> good_list = new ArrayList<>();
                // 商品迭代器
                Iterator iterator = goodCars.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    GoodCar goodCar = (GoodCar) next;
                    // 如果是曾经记录的总价 就记录 在循环后删除
                    if(goodCar.getSum()!= null){
                        bool_sum = goodCar.getId();
                        goodCarSum = goodCar;
                        continue;
                    }
                    String GoodBrand = goodsMapper.selectById(goodCar.getGoodId()).getGoodBrand();
                    String GoodType = goodsMapper.selectById(goodCar.getGoodId()).getGoodType();
                    if (activity.getGoodType().equals(GoodType) && activity.getGoodBrand().equals(GoodBrand)) {
                        // 加入good_list
                        good_list.add(goodCar);
                        // 往sum_list加钱（有减价和打折要加活动价
                        Double activityPrice = goodsMapper.selectById(goodCar.getGoodId()).getActivityPrice();
                        if (activityPrice != null) {
                            sum_list += activityPrice * goodCar.getGoodNum();
                        }
                        if (activityPrice == null) {
                            // 根据goodCar的goodId获取good的Price
                            Double price = goodsMapper.selectById(goodCar.getGoodId()).getPrice();
                            sum_list += price * goodCar.getGoodNum();
                        }
                    }
                }
                // 如果是sum的那条记录，先删掉
                if(goodCarSum.getSum()!=null){
                    goodCars.remove(goodCarSum);
                }

                // 如果够满减
                if (sum_list > enough || sum_list == enough) {
                    // 1 把sum_list加入总sum
                    sum_list -= cut;
                    sum += sum_list;
                    // 2 把物品从总list删去
                    iterator = goodCars.iterator();
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        GoodCar goodCar1 = (GoodCar) next;
                        goodCars.remove(goodCar1);
                    }
                }//如果没有达到满减金额就还是放在总list里
            }

        } // 2计算没有参加满减的总list
        Iterator iterator1 = goodCars.iterator();
        while (iterator1.hasNext()) {
            Object next = iterator1.next();
            GoodCar goodCar = (GoodCar) next;
            Double activityPrice = goodsMapper.selectById(goodCar.getGoodId()).getActivityPrice();
            if (activityPrice != null) {
                sum += activityPrice * goodCar.getGoodNum();
            }
            if (activityPrice == null) {
                // 根据goodCar的goodId获取good的Price
                Double price = goodsMapper.selectById(goodCar.getGoodId()).getPrice();
                sum += price * goodCar.getGoodNum();
            }
        }
        GoodCarSum goodCarSum1 = new GoodCarSum(bool_sum,sum);
        return goodCarSum1;
    }

    /**
     * 增加订单记录，用户购买后，记录用户的购买信息（忽略用户付款，后续增加）
     *
     * @param goodCar
     */
    @Override
    @Transactional(rollbackFor = NotEnoughException.class)
    // goodCar 传入要购买的部分
    public void orders(List<GoodCar> goodCar, HttpServletRequest httpServletRequest) throws NotEnoughException {

        // 1.创建订单
        //获取该用户最后一个userOrderId
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Orders> orderList = ordersMapper.selectList(queryWrapper);
        // 该用户从没有订单，则新的订单编号是1
        int userOrderId = 1;
        // 已经有订单，新的订单编号是（最后一个订单的编号+1）
        if (!orderList.isEmpty()) {
            userOrderId = orderList.get(orderList.size() - 1).getUserOrderId().intValue() + 1;
        }
        Iterator iterator = goodCar.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            GoodCar goodCar1 = (GoodCar) next;
            Orders order = new Orders();
            order.setGoodId(goodCar1.getGoodId());
            order.setGoodNum(goodCar1.getGoodNum());
            order.setUserId(goodCar1.getUserId());
            order.setUserOrderId(userOrderId);
            ordersMapper.insert(order);
        }
        // 2.计算满减得到价格,在order表插入带总金额的记录
        GoodCarSum goodCarSum1 = this.goodCarSum(httpServletRequest,goodCar);
        Orders order = new Orders();
        order.setUserOrderId(userOrderId);
        order.setSumPrice(goodCarSum1.getSum());
        ordersMapper.insert(order);
        //3.更新购物车
        //原本的购物车
        QueryWrapper<GoodCar> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<GoodCar> oldGoodCarList = goodCarMapper.selectList(queryWrapper1);
        for (GoodCar newGoodCar : goodCar) {
            for (GoodCar oldGoodCar : oldGoodCarList) {
                if (oldGoodCar.getGoodId() == newGoodCar.getGoodId()) {
                    //全买
                    int oldNum = oldGoodCar.getGoodNum().intValue();
                    int newNum = newGoodCar.getGoodNum().intValue();
                    if (oldNum == newNum) {
                        UpdateWrapper<GoodCar> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("user_id", user.getId())
                                .eq("good_id", oldGoodCar.getGoodId());
                        goodCarMapper.delete(updateWrapper);

                    } else if (newNum < oldNum) {//买部分
                        oldGoodCar.setGoodNum(oldGoodCar.getGoodNum() - newGoodCar.getGoodNum());
                        goodCarMapper.updateById(oldGoodCar);
                    } else if (newNum > oldNum) {
                        throw new NotEnoughException("超过购物车数量");
                    }
                    break;
                }
            }
        }

        //3更新库存
        for (GoodCar newGoodCar : goodCar
        ) {
            Goods goods = goodsMapper.selectById(newGoodCar.getGoodId());
            if (goods.getStock() < newGoodCar.getGoodNum()) {
                throw new NotEnoughException("超过库存数量");
            }
            goods.setStock(goods.getStock() - newGoodCar.getGoodNum());
            goodsMapper.updateById(goods);
        }
    }

    /**
     * 用户可以查看自己的订单
     */
    @Override
    public List<List<Orders>> retrieveOrders(HttpServletRequest httpServletRequest) {
        //session获取用户id
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Orders> ordersList = ordersMapper.selectList(queryWrapper);
        int userOrderId = ordersList.get(ordersList.size() - 1).getUserOrderId();
        List<List<Orders>> resultList = new LinkedList<>();
        for (int i = 1; i <= userOrderId; i++) {
            QueryWrapper<Orders> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", user.getId())
                    .eq("user_order_id", i);
            List<Orders> result = ordersMapper.selectList(queryWrapper1);
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 管理员可以查看任意用户的订单
     */
    @Override
    public List<Orders> retrieveAllOrders(Orders orders) {

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", orders.getUserId());
        List<Orders> ordersList = ordersMapper.selectList(queryWrapper);
        if (ordersList == null) {
            return null;
        }
        int userOrderId = ordersList.get(ordersList.size() - 1).getUserOrderId();
        List<List<Orders>> resultList = new LinkedList<>();
        for (int i = 1; i <= userOrderId; i++) {
            QueryWrapper<Orders> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", orders.getUserId())
                    .eq("user_order_id", i);
            List<Orders> result = ordersMapper.selectList(queryWrapper1);
            resultList.add(result);
        }
        return ordersList;
    }
}
