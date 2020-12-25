package com.lsh.serviceedu.service;

import com.lsh.serviceedu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
public interface EduVideoService extends IService<EduVideo> {
    //删除课程,先删video，再删chapter，最后删course
    boolean removeCourseById(String id);
}
