package com.example.outsourcingproject.order.dto.response;

import lombok.Getter;

@Getter
public class OrderAcceptResponseDto {

    private final String message;

    public OrderAcceptResponseDto(String message) {
        this.message = message;
    }
}
