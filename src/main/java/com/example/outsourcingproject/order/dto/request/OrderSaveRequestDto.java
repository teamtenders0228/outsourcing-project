package com.example.outsourcingproject.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderSaveRequestDto {

    private Long menuId;

    private Integer count;
}
