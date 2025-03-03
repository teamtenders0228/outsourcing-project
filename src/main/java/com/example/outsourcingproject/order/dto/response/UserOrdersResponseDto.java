package com.example.outsourcingproject.order.dto.response;

import com.example.outsourcingproject.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.order.enums.Status;
import lombok.Getter;

import java.util.List;

@Getter
public class UserOrdersResponseDto {

    private final String storeName;

    private final List<MenuResponseDto> menu;

    private final String category;

    private final Status status;

    private final String totalPrice;

    public UserOrdersResponseDto(String storeName, List<MenuResponseDto> menu, String category, Status status, String totalPrice) {
        this.storeName = storeName;
        this.menu = menu;
        this.category = category;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
