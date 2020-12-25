package com.lsh.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lsh.serviceedu.entity.query.CourseQuery;
import com.lsh.serviceedu.entity.vo.CourseInfoVo;
import com.lsh.serviceedu.entity.vo.CoursePublishVo;
import com.lsh.serviceedu.entity.vo.CourseQueryVo;
import com.lsh.serviceedu.entity.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程信息的基本方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程Id查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //更新课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程确认信息，多表联查
    CoursePublishVo getPublishCourseInfo(String id);

    //根据讲师id查询讲师所讲课程列表
    List<EduCourse> selectByTeacherId(String teacherId);

    //课程列表分页查询
    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    //删除课程
    boolean removeCourseById(String id);

    //分页课程查询列表（所有课程）
    Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery);

    //获取课程信息
    CourseWebVo selectInfoWebById(String id);

    //更新课程浏览数
    void updatePageViewCount(String id);

}
