package com.lsh.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.servicebase.exceptionhandler.EduException;
import com.lsh.serviceedu.entity.EduCourse;
import com.lsh.serviceedu.entity.EduCourseDescription;
import com.lsh.serviceedu.entity.query.CourseQuery;
import com.lsh.serviceedu.entity.vo.*;
import com.lsh.serviceedu.mapper.EduCourseMapper;
import com.lsh.serviceedu.service.EduChapterService;
import com.lsh.serviceedu.service.EduCourseDescriptionService;
import com.lsh.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsh.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程信息的基本方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1. 向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert == 0){
            //添加失败
            throw new EduException(20001,"添加课程失败");
        }

        //获取到添加之后的课程ID
        String cid = eduCourse.getId();

        //2. 向课程简介表加课程简介信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述ID，保证一对一关系
        eduCourseDescription.setId(cid);
        eduCourseDescriptionService.save(eduCourseDescription);

        return cid;
    }

    //根据课程Id查询课程全部信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //先查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //再查询描述表
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(courseDescription,courseInfoVo);
        //返回总封装信息
        return courseInfoVo;
    }


    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        //修改课程表
        int update = baseMapper.updateById(eduCourse);
        if(update == 0){
            throw new EduException(20001,"修改课程失败");
        }

        //修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo getPublishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }



    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if(courseQuery == null){
            baseMapper.selectPage(pageParam,queryWrapper);
            return;
        }
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.ge("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.ge("subject_id", subjectId);
        }
        baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public boolean removeCourseById(String id) {
        //根据id删除所有视频
        eduVideoService.removeCourseById(id);
        //根据id删除所有章节
        eduChapterService.removeByCourseId(id);
        //删除自身课程
        Integer result = baseMapper.deleteById(id);
        return result != null && result > 0;
    }

    @Override
    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseQuery.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",courseQuery.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseQuery.getSubjectId())){
            queryWrapper.eq("subject_id",courseQuery.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseQuery.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseQuery.getPriceSort())){
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam,queryWrapper);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public CourseWebVo selectInfoWebById(String id) {
        this.updatePageViewCount(id);
        return baseMapper.selectInfoWebById(id);
    }

    @Override
    public void updatePageViewCount(String id) {
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
    }

    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);

        List<EduCourse> eduCourses = baseMapper.selectList(queryWrapper);

        return eduCourses;
    }
}
