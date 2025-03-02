package com.example.outsourcingproject.domain.menu.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MenuSaveRequestDto {

    @NotBlank(message = "카테고리를 입력하세요.")
    private String category;

    @NotBlank(message = "메뉴이름을 입력하세요.")
    private String menuName;

    @NotBlank(message = "가격을 입력하세요.")
    private int price;

}
