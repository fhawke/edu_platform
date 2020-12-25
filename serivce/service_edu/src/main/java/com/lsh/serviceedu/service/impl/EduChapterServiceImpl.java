package com.lsh.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.servicebase.exceptionhandler.EduException;
import com.lsh.serviceedu.entity.EduChapter;
import com.lsh.serviceedu.entity.EduVideo;
import com.lsh.serviceedu.entity.vo.ChapterVo;
import com.lsh.serviceedu.entity.vo.VideoVo;
import com.lsh.serviceedu.mapper.EduChapterMapper;
import com.lsh.serviceedu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsh.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        //最终要的到的数据列表
        ArrayList<ChapterVo> chapterVoArrayList = new ArrayList<>();

        //获取章节信息
        QueryWrapper<EduChapter> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        queryWrapper1.orderByAsc("sort", "id");
        List<EduChapter> chapters = baseMapper.selectList(queryWrapper1);

        //获取课时信息
        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id", courseId);
        queryWrapper2.orderByAsc("sort", "id");
        List<EduVideo> videos = eduVideoService.list(queryWrapper2);

        //填充章节vo数据
        int count1 = chapters.size();
        for (int i = 0; i < count1; i++) {
            EduChapter chapter = chapters.get(i);

            //创建章节vo对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoArrayList.add(chapterVo);

            //填充课时vo数据
            ArrayList<VideoVo> videoVoArrayList = new ArrayList<>();
            int count2 = videos.size();
            for (int j = 0; j < count2; j++) {

                EduVideo video = videos.get(j);
                if(chapter.getId().equals(video.getChapterId())){
                    //创建课时vo对象
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoArrayList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoArrayList);
        }

        return chapterVoArrayList;
    }

    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId章节id，查询小节表，如果有数据，不进行删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);
        if(count > 0){
            throw new EduException(20001,"不能删除!");
        }else{
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }
    }
    //删除课程号下所有章节
    @Override
    public boolean removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        Integer count = baseMapper.delete(queryWrapper);
        return null != count && count > 0;
    }
}
