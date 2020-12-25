package com.lsh.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lsh.commonutils.util.JwtUtils;
import com.lsh.commonutils.util.MD5;
import com.lsh.educenter.entity.UcenterMember;
import com.lsh.educenter.entity.vo.LoginInfoVo;
import com.lsh.educenter.entity.vo.LoginVo;
import com.lsh.educenter.entity.vo.RegisterVo;
import com.lsh.educenter.mapper.UcenterMemberMapper;
import com.lsh.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsh.servicebase.exceptionhandler.EduException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-21
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //会员注册
    @Override
    public String register(RegisterVo registerVo) {
        //注册信息
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        if(StringUtils.isEmpty(mobile)||
           StringUtils.isEmpty(nickname)||
           StringUtils.isEmpty(password)||
           StringUtils.isEmpty(code)){
            return "error";
            //throw new EduException(20001,"请填写完整信息");
        }

        //校验验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobileCode)){
            return "error";
            //throw new EduException(20001,"验证码错误");
        }

        //查询数据库中是否存在相同手机号
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile",mobile));
        if(count.intValue() > 0){
            return "error";
            //throw new EduException(20001,"该号码已被注册");
        }

        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setIsDisabled(false);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        this.save(member);
        return "success";
    }

    //会员登陆
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new EduException(20001,"请填写完整信息");
        }

        //获取会员
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile",mobile));
        if(member == null){
            throw new EduException(20001,"该用户不存在");
        }

        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new EduException(20001,"密码错误");
        }

        //校验是否被禁用
        if(member.getIsDisabled()){
            throw new EduException(20001,"该用户已被禁用!");
        }

        //返回token令牌
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
        return token;

    }

    @Override
    public LoginInfoVo getLoginInfo(String memberId) {
        UcenterMember member = baseMapper.selectById(memberId);
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        BeanUtils.copyProperties(member,loginInfoVo);
        return loginInfoVo;
    }

    //查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
