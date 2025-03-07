package com.example.outsourcingproject.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponseDto {

    private final String bearerToken;

    public SigninResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
