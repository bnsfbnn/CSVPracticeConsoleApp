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

public class Product {
    private String id;
    private String name;
    private Double price;
    private Integer stockAvailable;
}
