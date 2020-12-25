package com.lsh.eduorder.service;

import com.lsh.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
public interface PayLogService extends IService<PayLog> {
    //生成微信支付二维码接口
    Map createNative(String orderNo);
    //向状态表添加记录，同时更新订单状态
    void updateOrdersStatus(Map<String, String> map);
    //根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
