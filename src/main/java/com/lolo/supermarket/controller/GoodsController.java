package com.lolo.supermarket.controller;

import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.dao.UserMapper;
import com.lolo.supermarket.entity.GoodCar;
import com.lolo.supermarket.entity.GoodRetrieveName;
import com.lolo.supermarket.entity.Goods;
import com.lolo.supermarket.entity.Orders;
import com.lolo.supermarket.exception.NotEnoughException;
import com.lolo.supermarket.service.GoodService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/good")
@RestController
public class GoodsController {
    @Autowired
    GoodService goodService;

    @Autowired
    UserMapper userMapper;


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
                goods.getPrice() <0 ||
                goods.getWeight() < 0 ||
                goods.getGoodBrand() == null||
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

    //查看购物车 计算总价


    //下订单
    @PostMapping("/orders")
    public Result orders(@RequestBody GoodCar[] goodCar, HttpServletRequest httpServletRequest) throws NotEnoughException {
        //参数错误
        if (goodCar.length == 0) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        for (int i = 0; i < goodCar.length; i++) {
            if (goodCar[i].getGoodNum() == null
                    || goodCar[i].getGoodNum() < 0
                    || goodCar[i].getGoodId() == null) {
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
