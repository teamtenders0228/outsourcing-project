package com.example.outsourcingproject.domain.store.dto.request;

import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

import java.time.LocalTime;
@Getter
public class StoreUpdateRequestDto {

    private String storeName;
    private String address;
    private String phone;
    private StoreCategory category;
    private Integer minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
}