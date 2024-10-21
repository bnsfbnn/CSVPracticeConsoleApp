package com.ntq.training.dal.entity;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.TreeMap;

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
}
