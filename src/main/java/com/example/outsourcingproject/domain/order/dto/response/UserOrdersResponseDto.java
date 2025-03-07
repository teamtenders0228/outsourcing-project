package com.example.outsourcingproject.domain.order.dto.response;

import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.menu.dto.response.MenuOrderResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserOrdersResponseDto {

    private final String storeName;

    private final List<MenuOrderResponseDto> menu;

    private final String category;

    private final Status status;

    private final String totalPrice;

    public UserOrdersResponseDto(String storeName, List<MenuOrderResponseDto> menu, String category, Status status, String totalPrice) {
        this.storeName = storeName;
        this.menu = menu;
        this.category = category;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
