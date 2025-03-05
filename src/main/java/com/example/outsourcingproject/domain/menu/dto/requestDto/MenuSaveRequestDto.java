package com.example.outsourcingproject.domain.menu.dto.requestDto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class MenuSaveRequestDto {

    @NotBlank(message = "메뉴이름을 입력하세요.")
    @Size(min = 1, max = 20, message = "메뉴는 1 ~ 20자 이어야 합니다.")
    private String menuName;

    @NotNull(message = "가격을 입력하세요.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

}
