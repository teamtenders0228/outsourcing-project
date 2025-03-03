package com.example.outsourcingproject.menu.dto.response;

import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final String name;

    private final String price;

    private final Integer count;

    public MenuResponseDto(String name, String price, Integer count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
