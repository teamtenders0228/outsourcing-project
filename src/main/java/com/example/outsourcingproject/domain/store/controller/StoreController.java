package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.domain.store.dto.request.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto>createStore(
           @Valid @RequestBody StoreCreateRequestDto dto){
        // 로그인 정보, 유저 등급 정보 추가 예정
        StoreResponseDto responseDto = storeService.createStore(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
