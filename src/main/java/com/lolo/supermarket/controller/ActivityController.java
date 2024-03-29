package com.lolo.supermarket.controller;
import com.lolo.supermarket.common.ResultEnum;
import com.lolo.supermarket.entity.Activity;
import com.lolo.supermarket.service.ActivityService;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/activity")
@RestController
public class ActivityController {
    @Resource
    ActivityService activityService;

    /**
     * 查看所有活动（包括未开始和已结束的）
     * 管理员特有
     * @return
     */
    @GetMapping("/retrieve-all")
    public Result retrieveAll() {
        List<Activity> data = activityService.retrieveAll();
        return ResultGenerator.successData(data);
    }

    /**
     * 查看进行中的活动
     */
    @GetMapping("/retrieve-two")
    public Result retrieveTwo(){
        List<Activity> data = activityService.retrieveTwo();
        return ResultGenerator.successData(data);
    }

    /**
     * 增加活动
     */
    @PostMapping("/create")
    public Result create(@RequestBody Activity activity) {
        //非空检查
        if (activity.getName() == null ||
                activity.getType() == null ||
                activity.getBeginTime() == null ||
                activity.getEndTime() == null ||
                activity.getGoodBrand() == null ||
                activity.getGoodType() == null) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        //检查商品种类是否固定的四个
        if (!activity.getGoodType().equals("男装") &&
                !activity.getGoodType().equals("女装") &&
                !activity.getGoodType().equals("手机") &&
                !activity.getGoodType().equals("美妆")
        ) {
            return ResultGenerator.fail(ResultEnum.PARAM_ERROR.getCode(),
                    ResultEnum.PARAM_ERROR.getMes());
        }
        int result = activityService.create(activity);
        if (result == -1) {
            return ResultGenerator.fail(ResultEnum.ACTIVI_ERROR2.getCode(),
                    ResultEnum.ACTIVI_ERROR2.getMes());
        } else if(result == -2){
            return ResultGenerator.fail(ResultEnum.ACTIVI_ERROR3.getCode(),
                    ResultEnum.ACTIVI_ERROR3.getMes());
        }
        else {
            return ResultGenerator.successData(activity.getId());
        }

    }
    /**
     * 根据id删除活动
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Activity activity) {
        int result = activityService.delete(activity.getId());
        if(result == -1){
            return ResultGenerator.fail(ResultEnum.ACTIVI_ERROR.getCode(),
                    ResultEnum.ACTIVI_ERROR.getMes());
        }else{
            return ResultGenerator.success();
        }
    }
}
