package com.lsh.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    //上传视频
    String uploadVideo(MultipartFile file);

    //删除视频
    void removeVideo(String videoId);

    //删除多个视频
    void removeMoreAlyVideo(List<String> videoIdList);
}