package com.example.outsourcingproject.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$",
            message = "새 비밀번호는 8자 이상이어야 하고, 숫자와 영문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String userRole;
}

