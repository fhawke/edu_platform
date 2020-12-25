package com.lsh.staservice.service;

import com.lsh.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-23
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {
    //统计某一天注册人数
    void registerCount(String day);
    //图片显示，返回两部分数据，一部分日期json数据，一部分数量json数组
    Map<String, Object> getShowData(String type, String begin, String end);

}
