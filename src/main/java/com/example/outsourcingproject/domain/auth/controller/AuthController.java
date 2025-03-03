package com.example.outsourcingproject.domain.auth.controller;

import com.example.outsourcingproject.common.response.MessageResponse;
import com.example.outsourcingproject.domain.auth.dto.SigninRequestDto;
import com.example.outsourcingproject.domain.auth.dto.SigninResponseDto;
import com.example.outsourcingproject.domain.auth.dto.SignupRequestDto;
import com.example.outsourcingproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public MessageResponse signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        authService.signup(signupRequestDto);
        return MessageResponse.of("회원가입에 성공하였습니다.");
    }

    @PostMapping("/signin")
    public SigninResponseDto signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return authService.signin(signinRequestDto);
    }

//    @DeleteMapping("/signout")
//    public void signout(){
//        return authService.signout();
//    }
}
