package com.hixtrip.sample.entry;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.dto.OrderReq;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * todo 这是你要实现的
 */
@RestController
public class OrderController {


    /**
     * todo 这是你要实现的接口
     *
     * @param commandOderCreateDTO 入参对象
     * @return 请修改出参对象
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PayDomainService payDomainService;

    @PostMapping(path = "/command/order/create")
    public String order(@RequestBody CommandOderCreateDTO commandOderCreateDTO) {
        //登录信息可以在这里模拟
        var userId = "";
        Order order =new Order();
        BeanUtils.copyProperties(commandOderCreateDTO,order);
        return "";
    }

    /**
     * todo 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * id 订单编号或者id
     * paymentType 支付类型: 0微信 1支付宝 2余额
     * payStatus 订单状态
     * type 类型: 0支付 1支付成功 2支付失败 3支付重复
     * amount 金额
     * userName 用户
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback")
    public Order payCallback(@RequestBody OrderReq orderReq) {
        return (Order)payDomainService.processPayment(orderReq.getId(), orderReq.getPaymentType(), orderReq.getPayStatus(),
                orderReq.getType(), orderReq.getAmount(), orderReq.getUserName());
    }

}
