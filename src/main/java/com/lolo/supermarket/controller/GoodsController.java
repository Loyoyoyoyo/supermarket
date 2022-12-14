package com.lolo.supermarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.dao.GoodCarMapper;
import com.lolo.supermarket.dao.GoodsMapper;
import com.lolo.supermarket.entity.Goods;
import com.lolo.supermarket.entity.UserCar;
import com.lolo.supermarket.service.GoodService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/good")
@RestController
public class GoodsController {
    @Autowired
    GoodService goodService;

    @Resource
    GoodsMapper goodsMapper;

    //3.商品需要按权重在首页进行展示，每类商品展示权重前2的，相同权重则优先选取创建时间晚的
    @GetMapping("/retrieve-all")
    public Result retrieveAll() {
        Goods[] data = goodService.selectAll();
        return ResultGenerator.successData(data);
    }

    //按id查询
    @PostMapping("/retrieve-id")
    public Result retrieveId(@RequestBody Goods good) {
        // 商品不存在
        if (goodsMapper.selectById(good.getId()) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        Goods data = goodService.selectById(good);
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
    public Result retrieveByName(@RequestBody Goods goods) {
        //输入为空
        if (goods.getGoodName() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        // 商品不存在
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("good_name", goods.getGoodName());
        if (goodsMapper.selectOne(queryWrapper) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }

        List<Goods> data = goodService.selectByName(goods.getGoodName(), goods.getGoodType());
        return ResultGenerator.successData(data);
    }

    //创建商品
    @PostMapping("/create")
    public Result create(@RequestBody Goods goods) {
        //输入为空
        if (goods.getGoodName() == null ||
                goods.getGoodType() == null) {

            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        //商品已存在
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("good_name", goods.getGoodName());
        if (goodsMapper.selectOne(queryWrapper) != null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR2.getCode(),
                    ResultEnum.GOOD_ERROR2.getMes());
        }
        goodService.addGood(goods);
        int id = goods.getId();
        return ResultGenerator.successMes(id);
    }

    //删除商品
    @PostMapping("/delete-id")
    public Result deleteById(@RequestBody Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        goodService.deleteById(goods);
        return ResultGenerator.success();
    }

    //修改商品权重
    @PostMapping("/update-weight")
    public Result updateWeight(@RequestBody Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        goodService.updateWeight(goods);
        return ResultGenerator.success();

    }

    //修改商品名称
    @PostMapping("/update-name")
    public Result updateName(@RequestBody Goods goods) {
        //输入为空
        if (goods.getGoodName() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        goodService.updateName(goods);
        return ResultGenerator.success();
    }

    //修改库存
    @PostMapping("/update-stock")
    public Result updateStock(@RequestBody Goods goods) {
        // 商品不存在
        if (goodsMapper.selectById(goods.getId()) == null) {
            return ResultGenerator.fail(ResultEnum.GOOD_ERROR.getCode(),
                    ResultEnum.GOOD_ERROR.getMes());
        }
        goodService.updateStock(goods);
        return ResultGenerator.success();
    }

    // 购物车：加入购物车
    @PostMapping("/create-car-good")
    public Result createCarGood(@RequestBody UserCar userCar) {
        int result = goodService.createCarGood(userCar);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.STOCK_ERROR.getCode(),
                    ResultEnum.STOCK_ERROR.getMes());
        } else {
            return ResultGenerator.success();
        }
    }

    //修改购物车内商品的数量
    @PostMapping("/update-car-good-num")
    public Result updateCarGoodNum(@RequestBody UserCar userCar) {
        goodService.updateCarGoodNum(userCar);
        return ResultGenerator.success();
    }
}
