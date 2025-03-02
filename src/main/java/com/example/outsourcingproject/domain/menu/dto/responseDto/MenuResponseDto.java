package com.example.outsourcingproject.domain.menu.dto.responseDto;

import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;

    private final String category;

    private final String menuName;

    private final int price;

    public MenuResponseDto(Long id, String category, String menuName, int price) {
        this.id = id;
        this.category = category;
        this.menuName = menuName;
        this.price = price;
    }
}
