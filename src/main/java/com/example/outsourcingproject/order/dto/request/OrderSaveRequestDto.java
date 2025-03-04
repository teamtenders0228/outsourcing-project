package com.example.outsourcingproject.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderSaveRequestDto {

    @NotNull(message = "반드시 값이 있어야 합니다.")
    private Long menuId;

    @NotNull(message = "반드시 값이 있어야 합니다.")
    private Integer count;
}
