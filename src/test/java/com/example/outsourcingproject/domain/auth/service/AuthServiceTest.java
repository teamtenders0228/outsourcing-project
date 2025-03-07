package com.example.outsourcingproject.domain.auth.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.config.JwtUtil;
import com.example.outsourcingproject.domain.auth.dto.request.SigninRequestDto;
import com.example.outsourcingproject.domain.auth.dto.request.SignupRequestDto;
import com.example.outsourcingproject.domain.auth.dto.response.SigninResponseDto;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_회원가입을_할_수_있다() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("텐더스", "aaa@aaa.com", "password11!", "010-1111-1111", "부산시 사상구", "USER");

        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);

        // when
        authService.signup(signupRequestDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void signin_로그인을_할_수_있다(){
        // given
        String password = "password11!";
        User user = new User("John Doe", "aaa@aaa.com",password,
                "010-1234-5678", "Seoul", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        SigninRequestDto signinRequestDto = new SigninRequestDto("aaa@aaa.com", password);

        String bearerToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())).willReturn(true);
        given(jwtUtil.createRefreshToken(user.getId())).willReturn(bearerToken);

        // when
        SigninResponseDto response = authService.signin(signinRequestDto);

        // then
        assertNotNull(response);
        assertEquals(bearerToken, response.getBearerToken());
    }
}