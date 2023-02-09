package com.lolo.supermarket.timetask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lolo.supermarket.dao.ActivityGoodMapper;
import com.lolo.supermarket.dao.ActivityMapper;
import com.lolo.supermarket.dao.GoodsMapper;
import com.lolo.supermarket.entity.Activity;
import com.lolo.supermarket.entity.ActivityGood;
import com.lolo.supermarket.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
@Slf4j
@Component
public class timeActivity {
    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    GoodsMapper goodsMapper;

    @Resource
    ActivityGoodMapper activityGoodMapper;

    //整点检查一次activity数据表（活动开始一般是整点）
//    @Scheduled(cron = "0 0 0/1 * * ? ")
    //测试用
    @Scheduled(cron = "* * 0/1 * * ? ")
    public void activity() {
        log.info("检查activity");
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        Integer[] status = {1,2};
        queryWrapper.in("status", status);
        List<Activity> activities = activityMapper.selectList(queryWrapper);
        Iterator iterator = activities.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            Activity activity = (Activity) next;
            Date date = new Date();
            // 1 活动状态为1（未开启）+活动开始时间早于或等于当前时间
            if ((activity.getBeginTime().equals(date) || activity.getBeginTime().before(date)) & activity.getStatus() == 1) {
                // 1.1 增加活动商品的记录
                // ①获取该活动涉及品牌和种类
                String goodBrand = activity.getGoodBrand();
                String goodType = activity.getGoodType();
                // ②获取当前活动商品的list
                QueryWrapper<Goods> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("good_brand", goodBrand);
                queryWrapper1.eq("good_type", goodType);
                List<Goods> goods = goodsMapper.selectList(queryWrapper1);
                // ③activityGood表中增加活动商品记录
                if (goods != null) {
                    Iterator<Goods> iterator1 = goods.iterator();
                    while (iterator1.hasNext()) {
                        Object next1 = iterator1.next();
                        Goods good = (Goods) next1;
                        ActivityGood activityGood = new ActivityGood(activity.getId(), good.getId());
                        activityGoodMapper.insert(activityGood);
                        //1.2 修改商品价格
                        // activity的活动类型是折扣还是降价还是满减(在购物车和订单进行操作）
                        // 解析activity的calculate  修改商品价格
                        // 折扣→降价→满减
                        // ①折扣
                        if (activity.getType().equals("折扣")) {
                            // 在activityGood 如果该商品已经降过价 重新按顺序：折扣→降价
                            QueryWrapper<Activity> queryWrapper2 = new QueryWrapper<>();
                            queryWrapper2.eq("good_brand", activity.getGoodBrand());
                            queryWrapper2.eq("good_type", activity.getGoodType());
                            queryWrapper2.eq("type", "降价");
                            Activity activity1 = activityMapper.selectOne(queryWrapper2);
                            if (activity1 != null) {
                                good.setActivityPrice(good.getPrice() * Double.parseDouble(activity.getCalculate()) - Double.parseDouble(activity1.getCalculate()));
                                goodsMapper.updateById(good);
                            } else {
                                // 该商品没有降过价，直接打折
                                good.setActivityPrice(good.getPrice() * Double.parseDouble(activity.getCalculate()));
                                goodsMapper.updateById(good);
                            }
                        }
                        // ②降价
                        if (activity.getType().equals("降价")) {
                            good.setActivityPrice(good.getPrice() - Double.parseDouble(activity.getCalculate()));
                            goodsMapper.updateById(good);
                        }

                    }
                }
                // 1.3 修改活动状态为2（进行中）
                activity.setStatus(2);
                activityMapper.updateById(activity);


            }
            // 2.活动结束时间早于或等于当前时间+活动状态为2（进行中）
            if ((activity.getEndTime().equals(date) || activity.getEndTime().before(date)) & activity.getStatus() == 2) {
                // 2.1 修改活动状态为3（已结束）
                activity.setStatus(3);
                activityMapper.updateById(activity);
                // 2.2 删除活动商品的记录
                QueryWrapper<ActivityGood> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("aid", activity.getId());
                activityGoodMapper.delete(queryWrapper3);
                // 2.3 修改商品价格
                // ①如果有一个以上活动，重新计算价格
                QueryWrapper<Activity> queryWrapper4 = new QueryWrapper<>();
                queryWrapper4.eq("good_brand", activity.getGoodBrand());
                queryWrapper4.eq("good_type", activity.getGoodType());
                queryWrapper4.eq("status", "2");
                String[] list = {"降价", "折扣"};
                queryWrapper4.in("type", list);
                Activity addtionActivity = activityMapper.selectOne(queryWrapper4);
                // 获取当前活动商品的list
                String goodBrand1 = activity.getGoodBrand();
                String goodType1 = activity.getGoodType();
                QueryWrapper<Goods> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("good_brand", goodBrand1);
                queryWrapper1.eq("good_type", goodType1);
                List<Goods> goods = goodsMapper.selectList(queryWrapper1);
                if (addtionActivity != null) {
                    if (addtionActivity.getType().equals("折扣")) {
                        Iterator iterator2 = goods.iterator();
                        while (iterator2.hasNext()) {
                            Object next2 = iterator2.next();
                            Goods good = (Goods) next2;
                            good.setActivityPrice(good.getPrice() * Double.parseDouble(addtionActivity.getCalculate()));
                            goodsMapper.updateById(good);
                        }
                    }
                    if (addtionActivity.getType().equals("降价")) {
                        Iterator iterator3 = goods.iterator();
                        while (iterator3.hasNext()) {
                            Object next3 = iterator3.next();
                            Goods good = (Goods) next3;
                            good.setActivityPrice(good.getPrice() - Double.parseDouble(addtionActivity.getCalculate()));
                            goodsMapper.updateById(good);
                        }
                    }
                } else {
                    // ②商品当前没活动，活动价置空
                    Iterator iterator4 = goods.iterator();
                    while (iterator4.hasNext()) {
                        Object next4 = iterator4.next();
                        Goods good = (Goods) next4;
                        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
                        goodsMapper.update(
                                null,
                                updateWrapper.<Goods>lambda()
                                        .set(Goods::getActivityPrice,null)
                        );
                    }
                }
            }
        }
    }
}
