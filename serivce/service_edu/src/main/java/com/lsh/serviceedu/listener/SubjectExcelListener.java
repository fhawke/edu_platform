package com.lsh.serviceedu.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.servicebase.exceptionhandler.EduException;
import com.lsh.serviceedu.entity.EduSubject;
import com.lsh.serviceedu.entity.excel.SubjectData;
import com.lsh.serviceedu.service.EduSubjectService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 监听器类，读取Excel
 * 该类无法交给Spring管理，因此我们需要自己new，无法注入其他对象
 * 不能实现数据库操作
 * 因此采用构造方法的形式进行注入
 *
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    public EduSubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        //读取Excel内容
        if(subjectData == null){
            throw new EduException(20001,"文件数据为空");
        }

        //一行一行读取，把分类加入数据库,先对一级分类进行处理，然后处理二级分类
        //二级分类的父ID是一级分类的本身ID(pid)
        EduSubject existOneSubject = existOneSubject(subjectService, subjectData.getOneSubjectName());
        if(existOneSubject == null){
            //没有相同对象，添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(existOneSubject);
        }
        String pid = existOneSubject.getId();
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(),pid);
        if(existTwoSubject == null){
            //没有相同对象，添加
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }
}
