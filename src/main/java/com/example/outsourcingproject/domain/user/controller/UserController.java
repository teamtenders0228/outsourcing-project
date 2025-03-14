package com.example.outsourcingproject.domain.user.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.ChangeProfileRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserResponseDto;
import com.example.outsourcingproject.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUser(@Auth AuthUser authUser) {
        return ResponseEntity.ok(userService.findUser(authUser.getId()));
    }

    @PatchMapping("/me/changePassword")
    public ResponseEntity<String> changePassword(
            @Auth AuthUser authUser,
            @Valid @RequestBody ChangePasswordRequestDto requestDto
    ) {
        userService.changePassword(authUser.getId(), requestDto);
        return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    @PatchMapping("/me/changeProfile")
    public ResponseEntity<UserResponseDto> changeProfile(
            @Auth AuthUser authUser,
            @Valid @RequestBody ChangeProfileRequestDto requestDto
    ) {
        return new ResponseEntity<>(userService.changeProfile(authUser.getId(), requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/me/delete")
    public ResponseEntity<String> deleteUser(
            @Auth AuthUser authUser,
            @Valid @RequestBody UserDeleteRequestDto requestDto
    ) {
        userService.deleteUser(authUser.getId(), requestDto.getPassword());
        return new ResponseEntity<>("성공적으로 탈퇴되었습니다.", HttpStatus.NO_CONTENT);
    }
}
