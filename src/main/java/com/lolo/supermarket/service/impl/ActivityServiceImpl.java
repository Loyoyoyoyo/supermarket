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
    public List<Activity> retrieveAll() {
        String[] types = {"满减", "折扣", "降价"};
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("type",types);
        List<Activity> activities = activityMapper.selectList(queryWrapper);
        return activities;
    }

    @Override
    public int create(Activity activity) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",activity.getName());
        //活动已存在
        if(activityMapper.selectOne(queryWrapper)!=null){
            return -1;
        }
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
