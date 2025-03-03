package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.store.entity.Store;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreResponseDto {
    private Long id;
    private String storeName;
    private String address;
    private String phone;
    private String category; // Enum -> String 변환
    private Integer minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double rating;
    private boolean closedFlag;

    // 명시적인 생성자 추가
    public StoreResponseDto(Long id, String storeName, String address, String phone, String category,
                            Integer minPrice, LocalTime openTime, LocalTime closeTime, Double rating, boolean closedFlag) {
        this.id = id;
        this.storeName = storeName;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.minPrice = minPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.rating = rating;
        this.closedFlag = closedFlag;
    }

    // 엔티티 → DTO 변환을 위한 정적 메서드
    public static StoreResponseDto fromEntity(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getPhone(),
                store.getCategory().name(), // Enum -> String 변환
                store.getMinPrice(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getRating(),
                store.isClosedFlag()
        );
    }
}
