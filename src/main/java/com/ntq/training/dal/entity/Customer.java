package com.ntq.training.dal.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
}
