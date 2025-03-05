package com.example.outsourcingproject.domain.menu.dto.requestDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private String menuName;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

}
