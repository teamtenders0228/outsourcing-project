package com.example.outsourcingproject.order.enums;

import java.util.Arrays;

public enum Status {
    PENDING, ACCEPT, COOKING, DELIVERY, COMPLETE, REJECT;

    public static com.example.outsourcingproject.order.enums.Status of(String status){
        return Arrays.stream(com.example.outsourcingproject.order.enums.Status.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Status"));
    }
}

