package com.example.outsourcingproject.domain.auth.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.config.JwtUtil;
import com.example.outsourcingproject.domain.auth.dto.SigninRequestDto;
import com.example.outsourcingproject.domain.auth.dto.SigninResponseDto;
import com.example.outsourcingproject.domain.auth.dto.SignupRequestDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.outsourcingproject.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(@Valid SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new BaseException(DUPLICATE_EMAIL, null);
        }

        if(signupRequestDto.getName() == null || signupRequestDto.getPhone() == null || signupRequestDto.getAddress() == null) {
            throw new BaseException(INVALID_FORM, null);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        UserRole userRole = UserRole.of(signupRequestDto.getUserRole());

        User user = new User(
                signupRequestDto.getName(),
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getPhone(),
                signupRequestDto.getAddress(),
                userRole
        );

        userRepository.save(user);
    }

    public SigninResponseDto signin(@Valid SigninRequestDto signinRequestDto) {

        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new BaseException(USER_NOT_EXIST, null));

        if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())){
            throw new BaseException(PASSWORD_MISMATCH, null);
        }

        String bearerToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());
        jwtUtil.createRefreshToken(user.getId());

        return new SigninResponseDto(bearerToken);
    }
}
