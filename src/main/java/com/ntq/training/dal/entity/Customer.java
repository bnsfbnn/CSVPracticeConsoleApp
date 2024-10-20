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

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
}
