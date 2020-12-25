package com.lsh.educenter.controller;


import com.lsh.commonutils.util.JwtUtils;
import com.lsh.commonutils.util.R;
import com.lsh.commonutils.util.ordervo.UcenterMemberOrder;
import com.lsh.educenter.entity.UcenterMember;
import com.lsh.educenter.entity.vo.LoginInfoVo;
import com.lsh.educenter.entity.vo.LoginVo;
import com.lsh.educenter.entity.vo.RegisterVo;
import com.lsh.educenter.service.UcenterMemberService;
import com.lsh.servicebase.exceptionhandler.EduException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-21
 */
@RestController
@RequestMapping("/educenter/member")

public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "会员登陆")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = ucenterMemberService.login(loginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        String msg = ucenterMemberService.register(registerVo);
        System.out.println(msg);
        if(msg.equals("success"))
        return R.ok().data("msg",msg);
        else{
            return R.error().data("msg",msg);
        }
    }

    @ApiOperation(value = "根据token令牌获取登陆信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try{
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            LoginInfoVo loginInfoVo = ucenterMemberService.getLoginInfo(memberId);
            return R.ok().data("item",loginInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new EduException(20001,"无法获取到token");
        }
    }

    //根据token字符串获取用户信息
    @PostMapping("getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id){
        //根据用户id获取用户登陆信息
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return ucenterMember;
    }

    //根据用户id获取到用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder memberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,memberOrder);
        return memberOrder;
    }

    //查询某一天的注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = ucenterMemberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }

}

