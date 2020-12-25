package com.lsh.eduorder.controller;


import com.lsh.commonutils.util.R;
import com.lsh.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
@RestController
@RequestMapping("/eduorder/paylog")

public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码
    //参数是订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //返回信息
        //二维码地址和其他信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("返回二维码map集合"+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    //参数:订单号，根据订单号查询支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("查询订单状态map集合"+map);
        if(map == null){
            return R.error().message("支付出错了");
        }
        //如果map不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，更新订单表状态
            payLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }
        //25000在前端拦截器做判断，表示支付中
        return R.ok().code(25000).message("支付中");
    }
}

