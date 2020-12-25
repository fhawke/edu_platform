package com.lsh.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.serviceedu.client.VodClient;
import com.lsh.serviceedu.entity.EduVideo;
import com.lsh.serviceedu.mapper.EduVideoMapper;
import com.lsh.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    //注入vodClient
    @Autowired
    private VodClient vodClient;

    @Override
    public boolean removeCourseById(String id) {



        //根据课程id查出所有所有视频id
        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id",id);
        queryWrapper2.select("video_source_id");
        List<EduVideo> EduVideoList = baseMapper.selectList(queryWrapper2);
        List<String> VideoIds = new ArrayList<>();
        for (EduVideo video : EduVideoList) {
            String videoSourceId = video.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId))
            VideoIds.add(videoSourceId);
        }
        //根据多个视频ID删除多个视频
        if(VideoIds.size()>0){
            vodClient.deleteBatch(VideoIds);
        }

        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", id);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }
}
