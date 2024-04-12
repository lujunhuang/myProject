package com.hixtrip.sample.client.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReq {
    private String id;
    private Integer paymentType;
    private String payStatus;
    private Integer type;
    private BigDecimal amount;
    private String userName;
}
