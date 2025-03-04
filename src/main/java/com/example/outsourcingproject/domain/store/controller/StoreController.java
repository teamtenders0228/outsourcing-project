package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.store.dto.request.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<String>createStore(
            @Auth AuthUser authUser,
            @Valid @RequestBody StoreCreateRequestDto dto
    ){
        storeService.createStore(authUser.getId(),dto);
        return new ResponseEntity<>("가게 등록에 성공 했습니다.", HttpStatus.CREATED);
    }

    @GetMapping
    public List<StoreResponseDto> getAllStores(){
      return storeService.getAllStores();
    }

    @GetMapping("/{storeId}")
    public StoreResponseDto getStoreById(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {
        return storeService.getStoreById(authUser.getId(), storeId);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<String>updateStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequestDto dto
    ){
        storeService.updateStore(authUser.getId(), storeId, dto);
        return new ResponseEntity<>("가게 정보 수정을 성공 했습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<String>deleteStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreDeleteRequestDto dto
    ){
        storeService.deleteStore(authUser.getId(), storeId, dto);
        return new ResponseEntity<>("가게를 폐업하였습니다.",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{storeId}/openClose")
    public ResponseEntity<String> toggleStoreStatus(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        String storeStatus = storeService.toggleStoreStatus(authUser.getId(), storeId);

        return new ResponseEntity<>(storeStatus, HttpStatus.OK);
    }
}
