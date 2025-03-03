package com.example.outsourcingproject.domain.user.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.response.MessageResponse;
import com.example.outsourcingproject.domain.user.dto.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.UserResponseDto;
import com.example.outsourcingproject.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUser(@Auth AuthUser authUser) {
        return ResponseEntity.ok(userService.getUser(authUser.getId()));
    }

//    @PatchMapping("/changePassword")
//    public MessageResponse changePassword(@Auth AuthUser authUser, @Valid ChangePasswordRequestDto requestDto) {
//
//    }

}
