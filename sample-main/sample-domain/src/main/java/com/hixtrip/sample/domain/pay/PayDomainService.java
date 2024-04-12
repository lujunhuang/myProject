package com.hixtrip.sample.domain.pay;

import com.hixtrip.sample.domain.factory.Payment;
import com.hixtrip.sample.domain.factory.PaymentFactory;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 支付领域服务
 * todo 不需要具体实现, 直接调用即可
 */
@Component
public class PayDomainService {


    /**
     * 记录支付回调结果
     * 【高级要求】至少有一个功能点能体现充血模型的使用。
     */
    public void payRecord(CommandPay commandPay) {
        //无需实现，直接调用即可
    }



    @Autowired
    private PaymentFactory paymentFactory;

    /**
     *  支付
     * @param id 订单编号或者id
     * @param paymentType 支付类型: 0微信 1支付宝 2余额
     * @param payStatus 订单状态
     * @param type 类型: 0支付 1支付成功 2支付失败 3支付重复
     * @param amount 金额
     * @param userName 用户
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object processPayment(String id, Integer paymentType, String payStatus, Integer type, BigDecimal amount, String userName) {
        Payment payment = paymentFactory.createPayment(paymentType);
        assert payment != null : "暂不支持";
        //类型: 0支付 1支付成功 2支付失败 3支付重复
        switch (type) {
            case 0:
                return payment.pay(id,amount,userName);
            case 1:
                return payment.paySuccess(id,payStatus,amount,userName);
            case 2:
                return payment.payFail(id,payStatus,amount,userName);
            case 3:
                return payment.payRepetition(id,payStatus,amount,userName);
            default:
                throw new RuntimeException("暂无此功能");
        }
    }

}
