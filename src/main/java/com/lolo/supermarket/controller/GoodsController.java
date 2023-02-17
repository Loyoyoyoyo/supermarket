package com.lolo.supermarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.dao.GoodCarMapper;
import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.*;
import com.lolo.supermarket.exception.NotEnoughException;
import com.lolo.supermarket.service.GoodService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/good")
@RestController
public class GoodsController {
    @Autowired
    GoodService goodService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GoodCarMapper goodCarMapper;

    //商品需要按权重在首页进行展示，每类商品展示权重前2的，相同权重则优先选取创建时间晚的

    @GetMapping("/retrieve-all")
    public Result retrieveAll() {
        Goods[] data = goodService.selectAll();
        return ResultGenerator.successData(data);
    }

    //按id查询
    @PostMapping("/retrieve-id")
    public Result retrieveId(@RequestBody Goods good) {
        //输入为空
        if (good.getId() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }

        Goods data = goodService.selectById(good);
        if (data == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        return ResultGenerator.successData(data);
    }


    //查询
    //按种类获取商品，并且按权重进行排序
    @PostMapping("/retrieve-type")
    public Result retrieveType(@RequestBody Goods goods) {
        //输入为空
        if (goods.getGoodType() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        //类别不存在
        String[] typeList = {"男装", "女装", "美妆", "手机"};
        for (String type : typeList) {
            if (type.equals(goods.getGoodType())) {
                List<Goods> data = goodService.selectByType(goods);
                return ResultGenerator.successData(data);
            }
        }
        return ResultGenerator.fail(ResultEnum.TYPE_EROOR.getCode(),
                ResultEnum.TYPE_EROOR.getMes());


    }

    //8.可以按名称搜索商品，并且按权重排序。支持 按种类搜索、不分种类搜索 两种方式String name, boolean byType, String type
    @PostMapping("/retrieve-name")
    public Result retrieveByName(@RequestBody GoodRetrieveName goodRetrieveName) {
        //输入为空
        if (goodRetrieveName.getGoodName() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        // 商品不存在
        List<Goods> data = goodService.selectByName(goodRetrieveName);
        if (data.isEmpty()) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        return ResultGenerator.successData(data);
    }

    //创建商品
    @PostMapping("/create")
    public Result create(@RequestBody Goods goods) {
        //输入为空
        if (goods.getGoodName() == null ||
                goods.getGoodType() == null ||
                goods.getStock() == null ||
                goods.getWeight() == null ||
                goods.getPrice() < 0 ||
                goods.getWeight() < 0 ||
                goods.getGoodBrand() == null ||
                goods.getStock() < 0) {

            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = goodService.addGood(goods);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR2.getCode(),
                    ResultEnum.GOOD_ERROR2.getMes());
        } else {
            int id = goods.getId();
            return ResultGenerator.successMes("商品的id为" + id);
        }
    }

    //删除商品
    @PostMapping("/delete-id")
    public Result deleteById(@RequestBody Goods goods) {
        //输入为空
        if (goods.getId() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }

        int result = goodService.deleteById(goods);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }

    //修改商品权重
    @PostMapping("/update-weight")
    public Result updateWeight(@RequestBody Goods goods) {
        //输入为空
        if (goods.getId() == null
                || goods.getWeight() == null
                || goods.getWeight() < 0) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        // 商品不存在

        int result = goodService.updateWeight(goods);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }

    //修改商品名称
    @PostMapping("/update-name")
    public Result updateName(@RequestBody Goods goods) {
        //输入为空
        if (goods.getId() == null || goods.getGoodName() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = goodService.updateName(goods);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        return ResultGenerator.success();
    }

    //修改库存
    @PostMapping("/update-stock")
    public Result updateStock(@RequestBody Goods goods) {
        //输入为空
        if (goods.getId() == null
                || goods.getStock() == null
                || goods.getStock() < 0) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = goodService.updateStock(goods);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }

    // 购物车：加入购物车
    @PostMapping("/create-car-good")
    public Result createCarGood(@RequestBody GoodCar goodCar, HttpServletRequest httpServletRequest) {
        // 参数错误
        if (goodCar.getGoodId() == null || goodCar.getGoodNum() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = goodService.createCarGood(goodCar, httpServletRequest);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        if (result == -2) {
            return ResultGenerator.fail(ResultEnum.STOCK_ERROR.getCode(),
                    ResultEnum.STOCK_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }

    //修改购物车内商品的数量
    @PostMapping("/update-car-good-num")
    public Result updateCarGoodNum(@RequestBody GoodCar goodCar, HttpServletRequest httpServletRequest) {
        //参数错误
        if (goodCar.getGoodNum() == null
                || goodCar.getGoodNum() < 0
                || goodCar.getGoodId() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = goodService.updateCarGoodNum(goodCar, httpServletRequest);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        } else if (result == -2) {
            return ResultGenerator.fail(ResultEnum.CAR_ERROR.getCode(),
                    ResultEnum.CAR_ERROR.getMes());
        } else if (result == -3) {
            return ResultGenerator.fail(ResultEnum.STOCK_ERROR.getCode(),
                    ResultEnum.STOCK_ERROR.getMes());
        } else {
            return ResultGenerator.success();

        }

    }

    //计算购物车总价
    //实际应该是自动发送请求，实时计算购物车总价
    @RequestMapping("/good-car-sum")
    public Result goodCarSum(HttpServletRequest httpServletRequest) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //获取该用户的购物车记录
        QueryWrapper<GoodCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        //总List：把满足满减的放进分list里计算价格，并从总List删去，最后计算没有参加满减的总list       的总价
        List<GoodCar> goodCars = goodCarMapper.selectList(queryWrapper);
        GoodCarSum goodCarSum = goodService.goodCarSum(httpServletRequest, goodCars);
        // 没算过总值 就插入
        if (goodCarSum.getBool_sum() == -1) {
            GoodCar goodCar = new GoodCar(user.getId(),
                    goodCarSum.getGoodNum(),
                    goodCarSum.getSum());
            goodCarMapper.insert(goodCar);
            return ResultGenerator.successData(goodCar);
        } else {//算过总值 就更新
            QueryWrapper<GoodCar> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("Id", goodCarSum.getBool_sum());
            GoodCar goodCar = goodCarMapper.selectOne(queryWrapper1);
            goodCar.setSum(goodCarSum.getSum());
            goodCarMapper.updateById(goodCar);
            return ResultGenerator.successData(goodCar);
        }

    }

    // TODO
    //下订单，可能买购物车的一部分东西
    @PostMapping("/orders")
    public Result orders(@RequestBody List<GoodCar> goodCar, HttpServletRequest httpServletRequest) throws NotEnoughException {
        //参数错误
        if (goodCar.size() == 0) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        Iterator iterator = goodCar.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            GoodCar goodCar1 = (GoodCar) next;
            if (goodCar1.getGoodNum() == null
                    || goodCar1.getGoodNum() < 0
                    || goodCar1.getGoodId() == null) {
                return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                        ResultEnum.PARAM_ERROR.getMes());
            }
        }
        goodService.orders(goodCar, httpServletRequest);
        return ResultGenerator.success();
    }


    //查看订单
    @GetMapping("/retrieve-orders")
    public Result retrieveOrders(HttpServletRequest httpServletRequest) {
        List<List<Orders>> result = goodService.retrieveOrders(httpServletRequest);
        if (result == null) {
            return ResultGenerator.fail(ResultEnum.ORDER_ERROR.getCode(),
                    ResultEnum.ORDER_ERROR.getMes());
        }
        return ResultGenerator.successData(result);
    }

    //查看任意订单
    @PostMapping("/retrieve-all-orders")
    public Result retrieveAllOrders(@RequestBody Orders orders) {
        //参数错误
        if (orders.getUserId() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        //用户不存在
        if (userMapper.selectById(orders.getUserId()) == null) {
            return ResultGenerator.fail(ResultEnum.USER_ERROR.getCode(),
                    ResultEnum.USER_ERROR.getMes());
        }
        //订单为空
        if (goodService.retrieveAllOrders(orders) == null) {
            return ResultGenerator.fail(ResultEnum.ORDER_ERROR.getCode(),
                    ResultEnum.ORDER_ERROR.getMes());
        } else {
            return ResultGenerator.successData(goodService.retrieveAllOrders(orders));
        }

    }
}
