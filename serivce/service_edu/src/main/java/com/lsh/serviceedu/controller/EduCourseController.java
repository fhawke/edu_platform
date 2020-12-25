package com.lsh.serviceedu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.commonutils.util.JwtUtils;
import com.lsh.commonutils.util.R;
import com.lsh.commonutils.util.ordervo.CourseWebVoOrder;
import com.lsh.servicebase.exceptionhandler.EduException;
import com.lsh.serviceedu.client.OrdersClient;
import com.lsh.serviceedu.entity.EduCourse;
import com.lsh.serviceedu.entity.query.CourseQuery;
import com.lsh.serviceedu.entity.vo.*;
import com.lsh.serviceedu.service.EduChapterService;
import com.lsh.serviceedu.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/serviceedu/course")

public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;

    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        //返回添加之后的课程ID
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程ID查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); //设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //课程列表 查询时间
    @ApiOperation(value = "分页查询课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    CourseQuery courseQuery){

        Page<EduCourse> pageParam = new Page<>(page, limit);

        courseService.pageQuery(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();

        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    //删除课程,先删video，再删chapter，最后删course
    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        boolean result = courseService.removeCourseById(id);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除失败");
        }
    }

    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "AllList/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody(required = false) CourseQueryVo courseQuery){
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.pageListWeb(pageParam, courseQuery);
        return  R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程详情")
    @GetMapping(value = "{courseId}")
    public R getById(@ApiParam(name = "courseId", value = "课程ID", required = true)
                         @PathVariable String courseId, HttpServletRequest request){
        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        //根据课程ID和用户ID查询当前课程已经支付
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);

        boolean buyCourse = ordersClient.isBuyCourse(courseId,memberIdByJwtToken);

        return R.ok().data("course",courseWebVo).data("chapterVoList",chapterVoList).data("isBuy",buyCourse);
    }

    //根据课程id查询课程信息
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo courseInfo = courseService.selectInfoWebById(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }

}

