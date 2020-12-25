package com.lsh.vod.controller;

import com.lsh.commonutils.util.R;
import com.lsh.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description="阿里云视频点播微服务")
@RestController
@RequestMapping("/eduvod/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    //上传视频
    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        String videoId = videoService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }


    //删除视频
    @DeleteMapping("{videoId}")
    public R removeVideo(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                         @PathVariable String videoId){

        videoService.removeVideo(videoId);
        return R.ok().message("视频删除成功");
    }

    //删除多个视频的方法
    //参数：多个视频id
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        videoService.removeMoreAlyVideo(videoIdList);
        return R.ok().message("视频删除成功");
    }
}
