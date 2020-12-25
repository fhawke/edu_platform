package com.lsh.serviceedu.client;


import com.lsh.commonutils.util.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    //定义调用方法的路径

    @DeleteMapping("/eduvod/video/{videoId}")
    public R removeVideo(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                         @PathVariable("videoId") String videoId);

    //定义调用删除多个视频的方法
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
