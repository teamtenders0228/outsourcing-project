package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.domain.auth.exception.InvalidFormException;
import com.example.outsourcingproject.domain.auth.exception.PasswordMismatchException;
import com.example.outsourcingproject.domain.auth.exception.UserNotExistException;
import com.example.outsourcingproject.domain.user.dto.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.ChangeProfileRequestDto;
import com.example.outsourcingproject.domain.user.dto.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.exception.DuplicatePasswordException;
import com.example.outsourcingproject.domain.user.exception.UserNotFoundException;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        return new UserResponseDto(user.getName(), user.getEmail(), user.getPhone(), user.getAddress());
    }

    public void changePassword(Long id, @Valid ChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

        if(user.getDeleteFlag()){
            throw new UserNotExistException();
        }
        if(!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchException();
        }

        if(requestDto.getOldPassword() == requestDto.getNewPassword()) {
            throw new DuplicatePasswordException();
        }

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    public void changeProfile(Long id, @Valid ChangeProfileRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());

        if(requestDto.getName() == null || requestDto.getPhone() == null || requestDto.getAddress() == null) {
            throw new InvalidFormException("잘못된 형식입니다.");
        }

        user.changeProfile(requestDto.getName(), requestDto.getPhone(), requestDto.getAddress());
    }

    public void deleteUser(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordMismatchException();
        }

        user.deleted();
    }
}
