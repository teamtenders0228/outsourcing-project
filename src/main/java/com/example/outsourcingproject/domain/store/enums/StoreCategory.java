package com.example.outsourcingproject.domain.store.enums;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

import java.util.Arrays;

public enum StoreCategory {
    KOREAN, JAPANESE, CHINESE, WESTERN, ASIAN, DESSERT;

    public static StoreCategory of(String category) {
        return Arrays.stream(StoreCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_CATEGORY_TYPE, category));
    }
}