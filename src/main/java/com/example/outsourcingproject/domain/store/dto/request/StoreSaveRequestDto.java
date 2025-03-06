package com.example.outsourcingproject.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;
@Getter
public class StoreSaveRequestDto {
    @NotBlank(message = "가게 이름은 필수 입력 값입니다.")
    private String storeName;
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone;
    @NotBlank(message = "가게 카테고리는 필수 입력 값입니다.")
    private String category;
    @NotNull(message = "최소 주문가격은 필수 입력 값입니다.")
    private Integer minPrice;
    @NotNull(message = "오픈시간은 필수 입력 값입니다.")
    private LocalTime openTime;
    @NotNull(message = "마감시간은 필수 입력 값입니다.")
    private LocalTime closeTime;
}
