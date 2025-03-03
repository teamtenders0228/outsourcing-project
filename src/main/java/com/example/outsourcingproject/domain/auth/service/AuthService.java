package com.example.outsourcingproject.domain.auth.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.config.JwtUtil;
import com.example.outsourcingproject.domain.auth.dto.SigninRequestDto;
import com.example.outsourcingproject.domain.auth.dto.SigninResponseDto;
import com.example.outsourcingproject.domain.auth.dto.SignupRequestDto;
import com.example.outsourcingproject.domain.auth.exception.InvalidFormException;
import com.example.outsourcingproject.domain.auth.exception.PasswordMismatchException;
import com.example.outsourcingproject.domain.auth.exception.UserNotExistException;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import com.example.outsourcingproject.domain.auth.exception.DuplicateEmailException;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(@Valid SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        if(signupRequestDto.getName() == null || signupRequestDto.getPhone() == null || signupRequestDto.getAddress() == null) {
            throw new InvalidFormException("회원가입 형식을 만족하지 않습니다.");
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
                () -> new UserNotExistException());

        if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())){
            throw new PasswordMismatchException();
        }

        String bearerToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponseDto(bearerToken);
    }

    public void signout() {

    }
}
