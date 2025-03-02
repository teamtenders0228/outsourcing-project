package com.example.outsourcingproject.order.dto.response;

import lombok.Getter;

@Getter
public class OrderRejectResponseDto {

    private final String message;

    public OrderRejectResponseDto(String message) {
        this.message = message;
    }
}
