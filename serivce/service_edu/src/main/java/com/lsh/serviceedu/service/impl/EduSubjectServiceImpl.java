package com.lsh.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.serviceedu.entity.EduSubject;
import com.lsh.serviceedu.entity.excel.SubjectData;
import com.lsh.serviceedu.entity.subject.OneSubject;
import com.lsh.serviceedu.entity.subject.TwoSubject;
import com.lsh.serviceedu.listener.SubjectExcelListener;
import com.lsh.serviceedu.mapper.EduSubjectMapper;
import com.lsh.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try{
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //课程分类列表(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询出所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //查询出所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> TwoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建List集合，用于存储最终数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //封装一级分类
        for(EduSubject eduSubject : oneSubjectList){
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //封装二级分类
            //在每个循环中反复查询1.id == 2.parent_id
            //然后把符合条件的二级分类封装成一个List，放入一级分类的元素中
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for(EduSubject eduSubject1 : TwoSubjectList){
                if(eduSubject1.getParentId().equals(eduSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject1,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }

            oneSubject.setChildren(twoFinalSubjectList);
            finalSubjectList.add(oneSubject);
        }
        return finalSubjectList;
    }
}
