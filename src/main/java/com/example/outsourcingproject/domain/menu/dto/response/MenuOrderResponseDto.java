package com.example.outsourcingproject.domain.menu.dto.response;

import lombok.Getter;

@Getter
public class MenuOrderResponseDto {

    private final String name;

    private final String price;

    private final Integer count;

    public MenuOrderResponseDto(String name, String price, Integer count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
