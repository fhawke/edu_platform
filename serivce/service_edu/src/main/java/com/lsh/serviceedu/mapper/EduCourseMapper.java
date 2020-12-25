package com.lsh.serviceedu.mapper;

import com.lsh.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsh.serviceedu.entity.vo.CoursePublishVo;
import com.lsh.serviceedu.entity.vo.CourseWebVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo getPublishCourseInfo(String id);
    CourseWebVo selectInfoWebById(String courseId);
}
