package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.store.dto.request.StoreSaveRequestDto;
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
    public ResponseEntity<StoreResponseDto> saveStore(
            @Auth AuthUser authUser,
            @Valid @RequestBody StoreSaveRequestDto dto
    ){
        StoreResponseDto storeResponseDto = storeOwnerService.saveStore(authUser.getId(),dto);
        return new ResponseEntity<>(storeResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getAllStores(@Auth AuthUser authUser){
        List<StoreResponseDto> storeResponseDtos = storeOwnerService.findAllStores(authUser.getId());
      return new ResponseEntity<>(storeResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStoreById(
            @PathVariable Long storeId,
            @Auth AuthUser authUser
    ) {
        StoreResponseDto storeResponseDto = storeOwnerService.findStoreById(storeId, authUser.getId());
        return new ResponseEntity<>(storeResponseDto, HttpStatus.OK);
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
