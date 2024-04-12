package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 订单领域服务
 * todo 只需要实现创建订单即可
 */
@Slf4j
@Component
public class OrderDomainService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * todo 需要实现
     * 创建待付款订单
     */
    public void createOrder(Order order) {
        //需要你在infra实现, 自行定义出入参、
        //生成订单编号DS(电商)
        String dsOrderNo = createNo("DS");
        order.setId(dsOrderNo);
        order.setPayTime(LocalDateTime.now());
        //====================================以下做插入订单操作================================
    }

    /**
     * todo 需要实现
     * 待付款订单支付成功
     */
    public Order orderPaySuccess(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参

        //1、校验订单selectOne(),用id和是否删除状态，进行查询得到Order。我这先new一个用
        Order order =new Order();
        if(ObjectUtils.isEmpty(order)){
            log.info("====================订单不存在:{}====================",commandPay.getOrderId());
            throw new RuntimeException("订单不存在");
        }
        //2、====================更新订单状态为支付成功======================================
        return order;

    }

    /**
     * todo 需要实现
     * 待付款订单支付失败
     */
    public Order orderPayFail(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参

        //1、校验订单selectOne(),用id和是否删除状态，进行查询得到Order。我这先new一个用
        Order order =new Order();
        if(ObjectUtils.isEmpty(order)){
            log.info("====================订单不存在:{}====================",commandPay.getOrderId());
            throw new RuntimeException("订单不存在");
        }
        //2、====================更新订单状态支付失败======================================
        return order;
    }

    /**
     * todo 需要实现
     * 待付款订单重复支付
     */
    public Order orderPayRepetition(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参

        //1、校验订单selectOne(),用id和是否删除状态，进行查询得到Order。我这先new一个用
        Order order =new Order();
        if(ObjectUtils.isEmpty(order)){
            log.info("====================订单不存在:{}====================",commandPay.getOrderId());
            throw new RuntimeException("订单不存在");
        }
        //2、====================更新订单状态支付失败======================================
        return order;
    }


    public String createNo(String prefix) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        String dateStr = currentDate.getYear() + String.format("%02d", currentDate.getMonth()) + String.format("%02d", currentDate.getDayOfMonth());
        String redisKey = prefix + dateStr ;
        Long count = redisTemplate.opsForValue().increment(redisKey);
        //设置过期时间
        redisTemplate.expire(redisKey, 86400, TimeUnit.SECONDS);
        String countStr = String.valueOf(count);
        StringBuilder sb = new StringBuilder();
        if (countStr.length() < 5) {
            for (int i = 0; i < 5 - countStr.length(); i++) {
                sb.append(0);
            }
            sb.append(countStr);
        } else {
            //如果不小于5位数 就只保留五位数
            sb.append(countStr.substring(countStr.length() - 5));
        }
        String lastNo = sb.toString();
        return prefix + dateStr + lastNo;
    }
}
