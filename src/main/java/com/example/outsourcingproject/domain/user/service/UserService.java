package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.domain.user.dto.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.ChangeProfileRequestDto;
import com.example.outsourcingproject.domain.user.dto.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.outsourcingproject.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));
        return new UserResponseDto(user.getName(), user.getEmail(), user.getPhone(), user.getAddress());
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDto requestDto) {
        log.info("Service, id= {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));

        log.info(user.getName());
        log.info("old:{}, new: {}", requestDto.getOldPassword(), requestDto.getNewPassword());

        if(user.getDeleteFlag()){ throw new BaseException(INACTIVE_USER, null); }
        if(!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new BaseException(PASSWORD_MISMATCH, null);
        }

        if(requestDto.getOldPassword().equals(requestDto.getNewPassword())) {
            throw new BaseException(PASSWORD_SAME_AS_OLD, null);
        }

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Transactional
    public void changeProfile(Long id, ChangeProfileRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));

        if(user.getDeleteFlag()){ throw new BaseException(INACTIVE_USER, null); }

        if(requestDto.getName() == null || requestDto.getPhone() == null || requestDto.getAddress() == null) {
            throw new BaseException(INVALID_FORM, null);
        }

        user.changeProfile(requestDto.getName(), requestDto.getPhone(), requestDto.getAddress());
    }

    @Transactional
    public void deleteUser(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));

        if(user.getDeleteFlag()){ throw new BaseException(INACTIVE_USER, null); }

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BaseException(PASSWORD_MISMATCH, null);
        }

        user.deleteUser();
    }
}
