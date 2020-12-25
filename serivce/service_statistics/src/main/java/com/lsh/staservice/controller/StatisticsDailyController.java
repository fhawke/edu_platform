package com.lsh.staservice.controller;


import com.lsh.commonutils.util.R;
import com.lsh.staservice.client.UcenterClient;
import com.lsh.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-23
 */
@RestController
@RequestMapping("/staservice/sta")

public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;
    //统计某一天注册人数
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }


    //图标显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,@PathVariable String end){
        Map<String,Object> map = statisticsDailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }
}

