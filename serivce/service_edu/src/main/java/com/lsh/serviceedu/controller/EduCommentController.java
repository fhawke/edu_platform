package com.lsh.serviceedu.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.commonutils.util.JwtInfo;
import com.lsh.commonutils.util.JwtUtils;
import com.lsh.commonutils.util.R;
import com.lsh.serviceedu.entity.EduComment;
import com.lsh.serviceedu.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论 前端控制器
 * @author ZengJinming
 * @since 2020-05-30
 */
@Api(description="课程评论相关")
@RestController
@RequestMapping("/eduservice/edu-comment")
@Slf4j
public class EduCommentController {
}


