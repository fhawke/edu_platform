package com.lsh.serviceedu.service;

import com.lsh.serviceedu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lsh.serviceedu.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
