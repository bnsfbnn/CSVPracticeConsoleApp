package com.ntq.training.dal.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder

public class Order {
    private String id;
    private String customerId;
    private Map<String, Integer> productQuantities;
    private OffsetDateTime orderDate;
    private BigDecimal totalAmount;
}
