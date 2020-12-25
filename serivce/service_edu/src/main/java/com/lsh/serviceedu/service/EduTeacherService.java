package com.lsh.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.serviceedu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lsh.serviceedu.entity.query.TeacherQuery;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-15
 */
public interface EduTeacherService extends IService<EduTeacher> {
    //分页查询管理
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    //用户前台分页显示
    public Map<String, Object> pageListWeb(Page<EduTeacher> pageParam);


}
