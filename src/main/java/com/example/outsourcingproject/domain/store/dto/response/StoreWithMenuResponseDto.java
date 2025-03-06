package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@JsonPropertyOrder({
        "storeName", "userName", "address", "phone", "category", "minPrice",
        "openTime", "closeTime", "rating", "menus"
})
@Getter
public class StoreWithMenuResponseDto {
    private String storeName;
    private String userName;
    private String address;
    private String phone;
    private String category;
    private Integer minPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double rating;
    private List<MenuResponseDto> menus;

    public StoreWithMenuResponseDto(Store store, List<Menu> menus) {
        this.storeName = store.getStoreName();
        this.userName = store.getUser().getName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.category = store.getCategory().name();
        this.minPrice = store.getMinPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.rating = store.getRating();
        this.menus = MenuResponseDto.toDtoList(menus);
    }
}
