package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.domain.user.dto.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("존재하는 사용자 정보 조회")
    void getUser() {
        // given
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe", "abcd1234!Q",
                "john@example.com", "010-1234-5678", "Seoul", UserRole.USER, false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        //when
        UserResponseDto responseDto = userService.getUser(userId);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getName()).isEqualTo("John Doe");
        assertThat(responseDto.getPhone()).isEqualTo("010-1234-5678");
        assertThat(responseDto.getAddress()).isEqualTo("Seoul");
    }
}