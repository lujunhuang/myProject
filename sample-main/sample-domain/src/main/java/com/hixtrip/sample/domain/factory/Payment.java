package com.hixtrip.sample.domain.factory;

import java.math.BigDecimal;

public abstract class Payment {


    /**
     * 调起支付
     *
     * @param id       订单id
     * @param amount   金额
     * @param userName 用户
     * @param <T>
     * @return
     */
    public abstract <T> T pay(String id, BigDecimal amount, String userName);

    /**
     * 支付成功
     *
     * @param id        订单id
     * @param payStatus 订单状态
     * @param amount    金额
     * @param userName  用户
     * @param <T>
     * @return
     */
    public abstract <T> T paySuccess(String id, String payStatus, BigDecimal amount, String userName);

    /**
     * 支付失败
     *
     * @param id        订单id
     * @param payStatus 订单状态
     * @param amount    金额
     * @param userName  用户
     * @param <T>
     * @return
     */
    public abstract <T> T payFail(String id, String payStatus, BigDecimal amount, String userName);

    /**
     * 支付重复
     *
     * @param id        订单id
     * @param payStatus 订单状态
     * @param amount    金额
     * @param userName  用户
     * @param <T>
     * @return
     */
    public abstract <T> T payRepetition(String id, String payStatus, BigDecimal amount, String userName);
}
