package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreUserController {
    private final StoreUserService storeUserService;

    @GetMapping
    public List<StoreResponseDto> getAllStores(){
        return storeUserService.getAllStores();
    }

    @GetMapping("/{storeId}")
    public StoreResponseDto getStoreById(
            @PathVariable Long storeId) {
        return storeUserService.getStoreById(storeId);
    }
}
