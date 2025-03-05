package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.store.dto.request.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/stores")
@RequiredArgsConstructor
public class StoreOwnerController {
    private final StoreOwnerService storeOwnerService;

    @PostMapping
    public ResponseEntity<String>createStore(
            @Auth AuthUser authUser,
            @Valid @RequestBody StoreCreateRequestDto dto
    ){
        storeOwnerService.createStore(authUser.getId(),dto);
        return new ResponseEntity<>("가게 등록에 성공 했습니다.", HttpStatus.CREATED);
    }

    @GetMapping
    public List<StoreResponseDto> getAllStores(@Auth AuthUser authUser){
      return storeOwnerService.getAllStores(authUser.getId());
    }

    @GetMapping("/{storeId}")
    public StoreResponseDto getStoreById(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {
        return storeOwnerService.getStoreById(storeId, authUser.getId());
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<String>updateStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreUpdateRequestDto dto
    ){
        storeOwnerService.updateStore(authUser.getId(), storeId, dto);
        return new ResponseEntity<>("가게 정보 수정을 성공 했습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<String>deleteStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreDeleteRequestDto dto
    ){
        storeOwnerService.deleteStore(authUser.getId(), storeId, dto);
        return new ResponseEntity<>("가게를 폐업하였습니다.",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{storeId}/openClose")
    public ResponseEntity<String> toggleStoreStatus(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        String storeStatus = storeOwnerService.toggleStoreStatus(authUser.getId(), storeId);

        return new ResponseEntity<>(storeStatus, HttpStatus.OK);
    }
}
