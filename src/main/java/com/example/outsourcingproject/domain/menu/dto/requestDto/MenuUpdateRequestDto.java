package com.example.outsourcingproject.domain.menu.dto.requestDto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class MenuUpdateRequestDto {

    private String menuName;

    @Pattern(regexp = "^[0-9]+$", message = "가격은 숫자만 포함되어야 합니다.")
    private String price;

}
