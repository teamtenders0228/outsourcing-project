package com.example.outsourcingproject.domain.order.enums;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

import java.util.Arrays;

public enum Status {
    PENDING, ACCEPT, COOKING, DELIVERY, COMPLETE, REJECT;

    public static Status of(String status){
        return Arrays.stream(Status.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_TYPE, null));
    }
}

