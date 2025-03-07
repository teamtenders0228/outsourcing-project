package com.example.outsourcingproject.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreDeleteRequestDto {
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
