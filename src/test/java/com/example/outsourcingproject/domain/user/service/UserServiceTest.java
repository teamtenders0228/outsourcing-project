package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
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
    void getUser_존재하는_사용자_정보_조회를_성공한다() {
        // given
        Long userId = 1L;
        User mockUser = new User("John Doe", "abcd1234!Q",
                "john@example.com", "010-1234-5678", "Seoul", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
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
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String preEncodedPassword = "preEncodedPassword";
        String encodedPassword = "encodedPassword";
        ChangePasswordRequestDto request = new ChangePasswordRequestDto(oldPassword, newPassword);
        User mockUser = new User("John Doe", "abcd1234!Q",
                "john@example.com", "010-1234-5678", "Seoul", UserRole.USER);
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
        given(mockUser.getDeleteFlag()).willReturn(false);
        given(passwordEncoder.matches(newPassword, mockUser.getPassword())).willReturn(false);
        given(passwordEncoder.matches(oldPassword, mockUser.getPassword())).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

        // when
        userService.changePassword(mockUser.getId(), request);

        // then
        then(userRepository).should(times(1)).findById(1L);
        then(passwordEncoder).should().matches(newPassword, preEncodedPassword);
        then(passwordEncoder).should().matches(oldPassword, preEncodedPassword);
        then(passwordEncoder).should().encode(newPassword);
        assertEquals(encodedPassword, mockUser.getPassword());
    }
}