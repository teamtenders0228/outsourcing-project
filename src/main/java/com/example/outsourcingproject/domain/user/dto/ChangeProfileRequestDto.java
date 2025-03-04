package com.example.outsourcingproject.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProfileRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;
}
