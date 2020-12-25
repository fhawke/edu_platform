package com.lsh.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.lsh.eduorder.entity.Order;
import com.lsh.eduorder.entity.PayLog;
import com.lsh.eduorder.mapper.PayLogMapper;
import com.lsh.eduorder.service.OrderService;
import com.lsh.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsh.eduorder.utils.HttpClient;
import com.lsh.servicebase.exceptionhandler.EduException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author lsh
 * @since 2020-12-22
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成微信支付二维码接口
    @Override
    public Map createNative(String orderNo) {
        try{
            //根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);
            //使用map设置生成二维码参数
            Map map = new HashMap();
            map.put("appid", "wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());
            map.put("out_trade_no", orderNo);
            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");

            //发送httpclient请求，传递参数(xml)
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式参数
            client.setXmlParam(WXPayUtil.generateSignedXml(map,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //执行请求发送
            client.post();
            //得到发送请求返回结果
            //返回内容是xml格式
            String xml = client.getContent();
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);

            //resultMap中只有二维码地址，我们需要返回更多信息，所以要进行封装
            Map trueMap = new HashMap();
            trueMap.put("out_trade_no", orderNo);
            trueMap.put("course_id", order.getCourseId());
            trueMap.put("total_fee", order.getTotalFee());
            trueMap.put("result_code", resultMap.get("result_code"));       //二维码操作状态码
            trueMap.put("code_url", resultMap.get("code_url"));             //二维码地址

            return trueMap;
        }catch (Exception e){
            throw new EduException(20001,"生成二维码失败!");
        }
    }
    //添加支付记录和更新订单状态
    @Override
    public void updateOrdersStatus(Map<String, String> map) {
        //从map获取订单号
        String orderNo = map.get("out_trade_no");
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);
        //更新订单表状态
        if(order.getStatus().intValue() == 1){
            return;
        }else{
            order.setStatus(1);     //1代表已经支付，0代表未支付
            orderService.updateById(order);
            //记录支付日志
            PayLog payLog=new PayLog();
            payLog.setOrderNo(order.getOrderNo());//支付订单号
            payLog.setPayTime(new Date());
            payLog.setPayType(1);//支付类型
            payLog.setTotalFee(order.getTotalFee());//总金额(分)
            payLog.setTradeState(map.get("trade_state"));//支付状态
            payLog.setTransactionId(map.get("transaction_id"));
            payLog.setAttr(JSONObject.toJSONString(map));
            baseMapper.insert(payLog);//插入到支付日志表
        }
    }
    //查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
