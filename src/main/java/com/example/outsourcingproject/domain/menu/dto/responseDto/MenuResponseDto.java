package com.example.outsourcingproject.domain.menu.dto.responseDto;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;

    private final String menuName;

    private final String price;

    public MenuResponseDto(Long id, String menuName, String price) {
        this.id = id;
        this.menuName = menuName;
        this.price = price;
    }

    // 조회
    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }


}
