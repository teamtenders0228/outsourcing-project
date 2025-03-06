package com.example.outsourcingproject.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;
}
