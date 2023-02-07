package com.lolo.supermarket.timetask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    //@Scheduled(cron = "0 0 0/1 * * ? ")
    //测试用
    @Scheduled(cron = "* * 0/1 * * ? ")
    public void activity(){
        log.info("检查activity");
        // 1 活动状态为1（未开启）+活动开始时间早于或等于当前时间
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<Activity> activities = activityMapper.selectList(queryWrapper);
        Iterator iterator = activities.iterator();
        while (iterator.hasNext()) {
            Object next =  iterator.next();
            Activity activity = (Activity) next;
            Date date = new Date();
            if((activity.getBeginTime().equals(date)||activity.getBeginTime().before(date))& activity.getStatus()==1){
                // 1.1 修改活动状态为2（进行中）
                activity.setStatus(2);
                activityMapper.updateById(activity);
                // 1.2 增加活动商品的记录
                // ①获取该活动涉及品牌和种类
                String goodBrand = activity.getGoodBrand();
                String goodType = activity.getGoodType();
                // ②获取当前活动商品的list
                QueryWrapper<Goods> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("good_brand",goodBrand);
                queryWrapper1.eq("good_type",goodType);
                List<Goods> goods = goodsMapper.selectList(queryWrapper1);
                // ③activityGood表中增加活动商品记录
                if(goods!=null){
                    Iterator<Goods> iterator1 = goods.iterator();
                    while (iterator1.hasNext()) {
                        Object next1 =  iterator1.next();
                        Goods good = (Goods)next1;
                        ActivityGood activityGood = new ActivityGood(activity.getId(),good.getId());
                        activityGoodMapper.insert(activityGood);
                    }
                }
                //1.3 修改商品价格
                // ①activity的活动类型是折扣还是降价还是满减
                // ①解析activity的calculate
                // 修改商品价格
            }

        }

        // 2.活动结束时间早于或等于当前时间+活动状态为2（进行中）→3（已结束）
    }

}
