package com.lsh.serviceedu.controller;


import com.lsh.commonutils.util.R;
import com.lsh.serviceedu.entity.subject.OneSubject;
import com.lsh.serviceedu.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/serviceedu/subject")

public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取到上传的文件，把文件内容读取
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传的Excel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    //课程分类列表功能树形结构
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }


}

