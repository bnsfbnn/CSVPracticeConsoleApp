package com.ntq.training.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CustomerToDeleteDTO {
    private String phoneNumber;
}
