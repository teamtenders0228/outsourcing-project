package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findUser_존재하는_사용자_정보_조회를_성공한다() {
        // given
        Long userId = 1L;
        User user = new User("John Doe", "john@example.com","abcd1234!Q",
                 "010-1234-5678", "Seoul", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        UserResponseDto responseDto = userService.findUser(userId);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getName()).isEqualTo("John Doe");
        assertThat(responseDto.getPhone()).isEqualTo("010-1234-5678");
        assertThat(responseDto.getAddress()).isEqualTo("Seoul");
    }

    @Test
    void changePassword_비밀번호_변경_성공한다() {
        // given
        String oldPassword = "oldPassword11!";
        String newPassword = "newPassword11!";

        ChangePasswordRequestDto request = new ChangePasswordRequestDto(oldPassword, newPassword);
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("John Doe", "john@example.com", oldPassword,
                "010-1234-5678", "Seoul", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn("encodedNewPassword");

        // when
        userService.changePassword(authUser.getId(), request);

        // then
        assertEquals(user.getPassword(), "encodedNewPassword");
    }

    @Test
    void deleteUserd에서_입력한_비밀번호와_기존비밀번호가_다를_경우_예외를_던진다() {
        // given
        String password = "Password11!";
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("John Doe", "john@example.com", password,
                "010-1234-5678", "Seoul", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        UserDeleteRequestDto userDeleteRequestDto = new UserDeleteRequestDto(password);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userDeleteRequestDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThrows(BaseException.class,
                () -> userService.deleteUser(authUser.getId(), userDeleteRequestDto.getPassword()),
                "잘못된 비밀번호입니다."
        );
    }
}