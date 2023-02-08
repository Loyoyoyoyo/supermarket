package com.lolo.supermarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lolo.supermarket.dao.ActivityMapper;
import com.lolo.supermarket.entity.Activity;
import com.lolo.supermarket.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    ActivityMapper activityMapper;

    @Override
    /**
     * 查看所有活动
     */
    public List<Activity> retrieveAll() {
        String[] types = {"满减", "折扣", "降价"};
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("type",types);
        List<Activity> activities = activityMapper.selectList(queryWrapper);
        return activities;
    }
    /**
     * 查看进行中的活动
     * @param
     * @return
     */
    @Override
    public List<Activity> retrieveTwo() {
        String[] types = {"满减", "折扣", "降价"};
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("type",types)
                    .eq("status",2);
        List<Activity> activities = activityMapper.selectList(queryWrapper);
        return activities;
    }




    @Override
    public int create(Activity activity) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",activity.getName());
        //活动已存在
        if(!activityMapper.selectList(queryWrapper).isEmpty()){
            return -1;
        }
        //同一品牌和类型的商品有一个以上同样类型的活动
        QueryWrapper<Activity> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("type",activity.getType());
        queryWrapper2.eq("goodType",activity.getGoodType());
        queryWrapper2.eq("goodBrand",activity.getGoodBrand());
        if(!activityMapper.selectList(queryWrapper2).isEmpty()){
            return -2;
        }
        //商品不会有一个以上同样type的活动
        activityMapper.insert(activity);
        return 0;
    }

    @Override
    public int delete(int id) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        if(activityMapper.selectOne(queryWrapper)==null){
            return -1;
        }
        activityMapper.deleteById(id);
        return 0;
    }
}
