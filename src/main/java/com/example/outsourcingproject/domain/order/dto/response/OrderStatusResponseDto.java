package com.example.outsourcingproject.domain.order.dto.response;

import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class OrderStatusResponseDto {

    private final String message;

    private final Long orderId;

    private final Long storeId;

    public OrderStatusResponseDto(String message, Long orderId, Long storeId) {
        this.message = message;
        this.orderId = orderId;
        this.storeId = storeId;
    }
}
