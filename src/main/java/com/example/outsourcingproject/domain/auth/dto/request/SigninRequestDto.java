package com.example.outsourcingproject.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
