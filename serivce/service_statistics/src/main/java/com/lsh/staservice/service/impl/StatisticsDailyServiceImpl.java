package com.lsh.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.commonutils.util.R;
import com.lsh.commonutils.util.ordervo.UcenterMemberOrder;
import com.lsh.staservice.client.UcenterClient;
import com.lsh.staservice.entity.StatisticsDaily;
import com.lsh.staservice.mapper.StatisticsDailyMapper;
import com.lsh.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-23
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    //远程调用得到某一天注册人数
    @Override
    public void registerCount(String day) {

        //添加记录之前先删除表相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //获取信息并添加到表中
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer)registerR.getData().get("countRegister");
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);  //注册人数
        sta.setDateCalculated(day);            //统计日期
        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(sta);
    }

    //图表显示两部分数据：日期json数组和数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //根据条件查询数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //数据封装 数据和数据对应数量
        //前端要求数组json结构
        //后端中，如果传对象就是json结构，如果传List就是json数组结构
        //因此定义2个List
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        for(int i = 0;i < staList.size();i++){
            StatisticsDaily daily = staList.get(i);
            //封装日期List
            date_calculatedList.add(daily.getDateCalculated());
            //封装对应数量
            switch (type){
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);

        return map;
    }

}
