package com.lsh.serviceedu.service;

import com.lsh.serviceedu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lsh.serviceedu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
public interface EduChapterService extends IService<EduChapter> {
    //得到所有章节，一级二级
    List<ChapterVo> nestedList(String courseId);
    //删除章节
    boolean deleteChapter(String chapterId);

    boolean removeByCourseId(String courseId);
}
