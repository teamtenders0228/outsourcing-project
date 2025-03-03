package com.example.outsourcingproject.order.dto.response;

import lombok.Getter;

@Getter
public class OrderSaveResponseDto {

    private final String message;

    public OrderSaveResponseDto(String message) {
        this.message = message;
    }
}
