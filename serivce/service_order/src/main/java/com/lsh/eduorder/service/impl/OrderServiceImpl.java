package com.lsh.eduorder.service.impl;

import com.lsh.commonutils.util.ordervo.CourseWebVoOrder;
import com.lsh.commonutils.util.ordervo.UcenterMemberOrder;
import com.lsh.eduorder.client.EduClient;
import com.lsh.eduorder.client.UcenterClient;
import com.lsh.eduorder.entity.Order;
import com.lsh.eduorder.mapper.OrderMapper;
import com.lsh.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsh.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //生成订单的方法
    @Override
    public String createOrders(String courseId, String memberIdByJwtToken) {
        //通过远程调用根据用户id获取到用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(courseId);
        //通过远程调用根据课程id获取到课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //添加到数据库
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);     //支付状态
        order.setPayType(1);    //支付类型：微信
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
