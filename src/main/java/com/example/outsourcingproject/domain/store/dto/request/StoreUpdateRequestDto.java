package com.example.outsourcingproject.domain.store.dto.request;

import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;
@Getter
public class StoreUpdateRequestDto {
    @NotBlank(message = "가게 이름은 필수 입력 값입니다.")
    private String storeName;
    @NotBlank(message = "가게 주소는 필수 입력 값입니다.")
    private String address;
    @NotBlank(message = "가게 번호는 필수 입력 값입니다.")
    private String phone;
    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private StoreCategory category;
    @NotNull
    @Min(value = 5000, message = "최소 주문 금액은 5000원 이상 입니다.")
    private Integer minPrice;
    @NotNull(message = "오픈시간은 필수 입력 값입니다.")
    private LocalTime openTime;
    @NotNull(message = "마감시간은 필수 입력 값입니다.")
    private LocalTime closeTime;
}