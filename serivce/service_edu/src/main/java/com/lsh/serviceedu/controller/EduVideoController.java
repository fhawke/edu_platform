package com.lsh.serviceedu.controller;


import com.lsh.commonutils.util.R;
import com.lsh.servicebase.exceptionhandler.EduException;
import com.lsh.serviceedu.client.VodClient;
import com.lsh.serviceedu.entity.EduVideo;
import com.lsh.serviceedu.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/serviceedu/edu-video")

public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    //TODO:需要完善:删除小节的时候，同时把阿里云里面的视频删除
    //调用vod模块删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //先根据小节id获取视频id,调用方法实现视频删除
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //根据视频id,远程调用实现视频删除
        if(!StringUtils.isEmpty(videoSourceId)){
            R result = vodClient.removeVideo(videoSourceId);
            if(result.getCode() == 20001){
                //熔断器执行
                throw new EduException(20001,"删除视频失败!  熔断器--------");
            }
        }

        //删除小节
        eduVideoService.removeById(id);
        return R.ok();
    }


    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo video){
        eduVideoService.updateById(video);
        return R.ok();
    }

    //根据小节ID查询
    @GetMapping("getVideoInfo/{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo video = eduVideoService.getById(id);
        return R.ok().data("video",video);
    }
}

