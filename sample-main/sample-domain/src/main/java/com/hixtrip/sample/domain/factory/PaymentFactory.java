package com.hixtrip.sample.domain.factory;


import com.hixtrip.sample.domain.factory.enums.PaymentEnums;
import com.hixtrip.sample.domain.pay.AliPayment;
import com.hixtrip.sample.domain.pay.BalancePayment;
import com.hixtrip.sample.domain.pay.WechatPayment;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class PaymentFactory {

    private final Map<String, Payment> paymentMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;
    @PostConstruct
    public void init(){
        paymentMap.put(PaymentEnums.ALIPAY.name(), applicationContext.getBean(AliPayment.class));
        paymentMap.put(PaymentEnums.WECHAT.name(), applicationContext.getBean(WechatPayment.class));
        paymentMap.put(PaymentEnums.BALANCE.name(), applicationContext.getBean(BalancePayment.class));
    }

    //类型: 0微信 1支付宝 2余额
    public Payment createPayment(Integer type) {
        switch (type) {
            case 0:
                return paymentMap.get(PaymentEnums.WECHAT.name());
            case 1:
                return paymentMap.get(PaymentEnums.ALIPAY.name());
            default:
                return paymentMap.get(PaymentEnums.BALANCE.name());
        }
    }
//
//    //类型: 0微信 1支付宝 2余额
//    public Payment createRefund(Integer type) {
//        switch (type) {
//            case 0:
//                return paymentMap.get(PaymentEnums.WECHAT.name());
//            case 1:
//                return paymentMap.get(PaymentEnums.ALIPAY.name());
//            default:
//                return paymentMap.get(PaymentEnums.BALANCE.name());
//        }
//    }

}
