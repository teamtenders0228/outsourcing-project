package com.example.outsourcingproject.domain.menu.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MenuSaveRequestDto {

    @NotBlank(message = "메뉴이름을 입력하세요.")
    @Size(min = 1, max = 20, message = "메뉴는 1 ~ 20자 이어야 합니다.")
    private String menuName;

    @NotBlank(message = "가격을 입력하세요.")
    @Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 포함되어야 합니다.")
    private String price;

}
