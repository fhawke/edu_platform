package com.lsh.serviceedu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.commonutils.util.R;
import com.lsh.serviceedu.entity.EduCourse;
import com.lsh.serviceedu.entity.EduTeacher;
import com.lsh.serviceedu.entity.query.TeacherQuery;
import com.lsh.serviceedu.service.EduCourseService;
import com.lsh.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-15
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/serviceedu/edu-teacher")

public class EduTeacherController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    /**
     * 测试环境，查询讲师表所有数据
     * rest风格
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R list(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    /**
     * 逻辑删除讲师
     */
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if(flag){
            return R.ok();
        }
        else{
            return R.error();
        }
    }

    /**
     * 条件查询讲师
     * @param current
     * @param limit
     * @param teacherQuery
     * @return
     */
    @ApiOperation(value = "分页讲师列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageQuery(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable Long current,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
                    @RequestBody(required = false) TeacherQuery teacherQuery){
            Page<EduTeacher> pageParam = new Page<>(current,limit);
            teacherService.pageQuery(pageParam,teacherQuery);
            List<EduTeacher> records = pageParam.getRecords();
            long total = pageParam.getTotal();

            return R.ok().data("total",total).data("rows",records);
    }

    /**
     * 新增讲师
     * @param teacher
     * @return
     */
    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R save(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody EduTeacher teacher){
        teacher.setGmtModified(new Date());
        teacherService.save(teacher);
        return R.ok();
    }

    /**
     * 根据ID查询讲师
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getById(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);

        //根据讲师id查询讲师讲课列表
        List<EduCourse> courses = courseService.selectByTeacherId(id);
        return R.ok().data("item",teacher).data("courList",courses);
    }

    /**
     * 修改讲师
     * @param
     * @param teacher
     * @return
     */
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    public R updateById(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody EduTeacher teacher){
        boolean flag = teacherService.updateById(teacher);
        if(flag)
        return R.ok();
        else
            return R.error();
    }

    /**
     * 用户前台页面显示
     */
    @ApiOperation(value = "分页显示列表")
    @GetMapping(value = "{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit){
        Page<EduTeacher> pageParam = new Page<>(page,limit);
        Map<String,Object> map = teacherService.pageListWeb(pageParam);
        return R.ok().data(map);
    }
}

