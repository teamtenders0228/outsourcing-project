package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String storeName;
    private String userName;
    private String address;
    private String phone;
    private String category; // Enum -> String 변환
    private Integer minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double rating;
    //private boolean closedFlag;
    private String username;


    // 엔티티 -> DTO 변환을 위한 정적 메서드
    public static StoreResponseDto fromEntity(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getUser().getName(),
                store.getStoreName(),
                store.getAddress(),
                store.getPhone(),
                store.getCategory().name(), // Enum -> String 변환
                store.getMinPrice(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getRating(),
      //          store.isClosedFlag(),
                store.getUser().getName()
        );
    }
}
