package com.example.outsourcingproject.domain.menu.dto.response;


import com.example.outsourcingproject.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;

    private final Long storeId;

    private final String menuName;

    private final String price;

    public MenuResponseDto(Long id, Long storeId, String menuName, String price) {
        this.id = id;
        this.storeId = storeId;
        this.menuName = menuName;
        this.price = price;
    }

    // 조회
    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getStore().getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }
}
