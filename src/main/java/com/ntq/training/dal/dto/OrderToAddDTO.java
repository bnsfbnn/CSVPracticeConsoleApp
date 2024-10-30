package com.ntq.training.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class OrderToAddDTO {
    private String customerId;
    private Map<String, Integer> productQuantities;
    private OffsetDateTime orderDate;
    private BigDecimal totalAmount;
}
