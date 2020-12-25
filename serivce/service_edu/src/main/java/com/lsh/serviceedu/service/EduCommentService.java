package com.lsh.serviceedu.service;

import com.lsh.serviceedu.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
public interface EduCommentService extends IService<EduComment> {

    //删除评论
    boolean deleteById(String commentId, String memberId);

    //判断是否是该用户的评论
    boolean isComment(String commentId, String memberId);
}
