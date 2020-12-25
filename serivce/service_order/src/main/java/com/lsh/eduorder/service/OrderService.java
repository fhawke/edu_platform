package com.lsh.eduorder.service;

import com.lsh.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
public interface OrderService extends IService<Order> {
    //生成订单
    String createOrders(String courseId, String memberIdByJwtToken);
}
