package com.lsh.educenter.service;

import com.lsh.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lsh.educenter.entity.vo.LoginInfoVo;
import com.lsh.educenter.entity.vo.LoginVo;
import com.lsh.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-21
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    LoginInfoVo getLoginInfo(String memberId);

    Integer countRegisterDay(String day);
}
