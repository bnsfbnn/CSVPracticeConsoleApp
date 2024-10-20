package com.ntq.training.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j

public class Order {
    private String id;
    private String customerId;
    private String productQuantities;
    private String orderDate;
}
