package com.ntq.training.dal.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder

public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
}
