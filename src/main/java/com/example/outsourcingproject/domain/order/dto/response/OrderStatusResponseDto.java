package com.example.outsourcingproject.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderStatusResponseDto {

    private final String message;

    public OrderStatusResponseDto(String message) {
        this.message = message;
    }
}
