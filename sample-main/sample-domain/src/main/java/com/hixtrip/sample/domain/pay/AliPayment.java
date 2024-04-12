package com.hixtrip.sample.domain.pay;

import com.hixtrip.sample.domain.factory.Payment;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 支付领域服务
 * todo 支付宝
 */
@Component
public class AliPayment extends Payment {

    private OrderDomainService orderDomainService;

    /**
     * todo (微信，支付宝，余额支付)调起支付都不一样
     */
    @Override
    public <T> T pay(String id, BigDecimal amount, String userName) {
        return null;
    }

    /**
     * todo 成功、失败、重复，我就(微信，支付宝，余额支付)先直接先全部写一样的，都调orderDomainService里面的 成功、失败、重复
     */
    @Override
    public Order paySuccess(String id, String payStatus, BigDecimal amount, String userName) {
        CommandPay commandPay = new CommandPay();
        commandPay.setOrderId(id);
        commandPay.setPayStatus(payStatus);
        return orderDomainService.orderPaySuccess(commandPay);
    }

    @Override
    public Order payFail(String id, String payStatus, BigDecimal amount, String userName) {
        CommandPay commandPay = new CommandPay();
        commandPay.setOrderId(id);
        commandPay.setPayStatus(payStatus);
        return orderDomainService.orderPayFail(commandPay);
    }

    @Override
    public Order payRepetition(String id, String payStatus, BigDecimal amount, String userName) {
        CommandPay commandPay = new CommandPay();
        commandPay.setOrderId(id);
        commandPay.setPayStatus(payStatus);
        return orderDomainService.orderPayRepetition(commandPay);
    }
}
