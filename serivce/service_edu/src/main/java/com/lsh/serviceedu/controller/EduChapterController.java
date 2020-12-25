package com.lsh.serviceedu.controller;


import com.lsh.commonutils.util.R;
import com.lsh.serviceedu.entity.EduChapter;
import com.lsh.serviceedu.entity.vo.ChapterVo;
import com.lsh.serviceedu.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/serviceedu/edu-chapter")

public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表，根据课程ID查询
    @ApiOperation(value = "章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedListByCourseId(
            @ApiParam(name = "courseId",value = "课程ID",required = true)
            @PathVariable String courseId){
        System.out.println("-----enter");
        List<ChapterVo> chapterVos = chapterService.nestedList(courseId);
        System.out.println(chapterVos);
        return R.ok().data("items",chapterVos);
    }


    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter edu = chapterService.getById(chapterId);
        return R.ok().data("chapter",edu);
    }

    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }
}

